package com.examples.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.Future;
import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
@Component
public class DummyWebSocketServlet extends WebSocketServlet{

    private static final long serialVersionUID = -7289719281366784056L;
    private final Set<DummySocket> members = new CopyOnWriteArraySet<DummySocket>();

    private static List<String> animals = new ArrayList<String>();
    static {
        animals.add("cat");animals.add("zebra");animals.add("rabbit");
        animals.add("lion");animals.add("fish");animals.add("camel");
        animals.add("dog");animals.add("tiger");animals.add("elephant");
    }
    ActorSystem actorSystem;
    ActorRef imageMaster;
    ObjectMapper mapper = new ObjectMapper();


    @Override
    public void init() throws ServletException {
        super.init();

        // we're not in a spring context for this servlet
        actorSystem = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext()).getBean("actorSystem", ActorSystem.class);
        imageMaster = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext()).getBean("imageMaster", ActorRef.class);

        // init scheduler form actor system
        actorSystem.scheduler().schedule(Duration.create(5, TimeUnit.SECONDS), Duration.create(5, TimeUnit.SECONDS), new Runnable() {
            @Override
            public void run() {
                // for each member ask the image master for an image
                for (final DummySocket member : members) {
                    // random animal
                    String animalName = animals.get((new Random()).nextInt(8));
                    // get an image with that animal
                    Future<Object> imageFuture =  Patterns.ask(imageMaster, animalName, new Timeout(Duration.create(10, TimeUnit.SECONDS)));
                    imageFuture.onSuccess(new OnSuccess<Object>() {
                        /**
                         * This method will be invoked once when/if a Future that this callback is registered on
                         * becomes successfully completed
                         */
                        @Override
                        public void onSuccess(Object result) {
                            if (member.isOpen()){
                                try{
                                    if (result != null) member.sendMessage(mapper.writeValueAsString(result));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    Future<Object> aggregateFuture = imageFuture.flatMap(new Mapper<Object, Future<Object>>() {
                        @Override
                        public Future<Object> apply(Object v1) {
                            return Patterns.ask(imageMaster, "aggregate", new Timeout(Duration.create(10, TimeUnit.SECONDS)));
                        }
                    });
                    aggregateFuture.onSuccess(new OnSuccess<Object>() {
                        /**
                         * This method will be invoked once when/if a Future that this callback is registered on
                         * becomes successfully completed
                         */
                        @Override
                        public void onSuccess(Object result) {
                            if (member.isOpen()){
                                try{
                                    if (result != null) member.sendMessage(mapper.writeValueAsString(result));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new DummySocket();
    }

    class DummySocket implements WebSocket.OnTextMessage, WebSocket.OnBinaryMessage {
        private Connection connection;
        @Override
        public void onClose(int closeCode, String message) {
            members.remove(this);
        }
        public void sendMessage(String data) throws IOException {
            connection.sendMessage(data);
        }
        @Override
        public void onMessage(String data) {
            System.out.println("Received: "+data);
        }
        public boolean isOpen() {
            return connection.isOpen();
        }

        @Override
        public void onOpen(Connection connection) {
            members.add(this);
            this.connection = connection;
            try {
                connection.sendMessage("Server received Web Socket upgrade and added it to Receiver List.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(byte[] data, int offset, int length) {
            try {
                connection.sendMessage(data, offset, length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
