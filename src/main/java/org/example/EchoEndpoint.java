package org.example;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnClose;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

@ServerEndpoint(value = "/echo")
public class EchoEndpoint {
    Session session;
    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("OnOpen");
        session.getBasicRemote().sendText("Yay connected!");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
        System.out.println("From client : " + message);
        session.getAsyncRemote().sendText("You are connected!");
    }

    @OnClose
    public void onClose(){
        System.out.println("Connection closed.");
    }
}
