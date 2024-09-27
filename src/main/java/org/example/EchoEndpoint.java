package org.example;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

@ServerEndpoint(value = "/echo")
public class EchoEndpoint {
    @OnOpen
    public void onOpen(Session session) throws IOException {
        Thread clientHandler = new Thread(new ClientHandler(session));
        clientHandler.start();
//        session.getBasicRemote().sendText("You are connected to server!");
    }
    @OnMessage
    public void onMessage(String message){
        System.out.println("From client : " + message);
    }
}
