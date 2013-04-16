package com.examples;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

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
public class DummyWebSocketServlet extends WebSocketServlet{

    private static final long serialVersionUID = -7289719281366784056L;
    public static String newLine = System.getProperty("line.separator");
    private final Set<DummySocket> _members = new CopyOnWriteArraySet<DummySocket>();
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void init() throws ServletException {
        super.init();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running Server Message Sending");
                for(DummySocket member : _members){
                    System.out.println("Trying to send to Member!");
                    if(member.isOpen()){
                        System.out.println("Sending!");
                        try {
                            member.sendMessage("Sending a Message to you Guys! "+new Date()+newLine);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 2, 2, TimeUnit.SECONDS);
    }

    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new DummySocket();
    }

    class DummySocket implements WebSocket.OnTextMessage {
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
    }
}