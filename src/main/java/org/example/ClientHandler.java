package org.example;

import jakarta.websocket.Session;

public class ClientHandler implements Runnable{
    private final Session session;

    public ClientHandler(Session session){
        this.session = session;
    }

    @Override
    public void run() {
        try{
            this.session.getBasicRemote().sendText("You are connected to the server!");
            System.out.println("Thread id: " + Thread.currentThread().threadId() + " is connected");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
