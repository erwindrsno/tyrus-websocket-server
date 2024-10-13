package org.example;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
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
        System.out.println("Threads count currently  : " + Thread.currentThread().getName());
        File file = new File("test.txt");
        if(file.exists()){
            try{
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                ByteBuffer bytebuffer = ByteBuffer.wrap(fileBytes);
                session.getBasicRemote().sendBinary(bytebuffer);
                System.out.println("SENT!");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("tak ada");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
        System.out.println("From client : " + message);
    }

    @OnClose
    public void onClose(){
        System.out.println("Connection closed.");
    }
}
