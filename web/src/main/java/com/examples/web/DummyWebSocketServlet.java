package com.examples.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.Future;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;
import com.examples.akka.service.DummyService;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
@Component
public class DummyWebSocketServlet extends WebSocketServlet{

    private static final long serialVersionUID = -7289719281366784056L;
    private final Set<DummySocket> _members = new CopyOnWriteArraySet<DummySocket>();

    ActorSystem actorSystem;
    ActorRef imageMaster;


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
                for (final DummySocket member : _members) {
                    Future<Object> future =  Patterns.ask(imageMaster, "echo", new Timeout(Duration.create(10, TimeUnit.SECONDS)));
                    future.onSuccess(new OnSuccess<Object>() {
                        /**
                         * This method will be invoked once when/if a Future that this callback is registered on
                         * becomes successfully completed
                         */
                        @Override
                        public void onSuccess(Object result) {
                            if (member.isOpen()){
                                try{
                                    member.sendMessage(result.toString());
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
        private Connection _connection;
        @Override
        public void onClose(int closeCode, String message) {
            _members.remove(this);
        }
        public void sendMessage(String data) throws IOException {
            _connection.sendMessage(data);
        }
        @Override
        public void onMessage(String data) {
            System.out.println("Received: "+data);
        }
        public boolean isOpen() {
            return _connection.isOpen();
        }

        @Override
        public void onOpen(Connection connection) {
            _members.add(this);
            _connection = connection;
            try {
                connection.sendMessage("Server received Web Socket upgrade and added it to Receiver List.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(byte[] data, int offset, int length) {
            try {
                _connection.sendMessage(data, offset, length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
