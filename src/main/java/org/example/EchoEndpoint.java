package org.example;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.concurrent.Future;

@ServerEndpoint(value = "/echo")
public class EchoEndpoint {
    Session session;
    @OnOpen
    public void onOpen(Session session) throws Exception {
        System.out.println("Thread : " + Thread.currentThread().getName());
        File file = new File("T06xxyyy.zip");
        final int CHUNK_SIZE = 10240;
        if(file.exists()){
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[CHUNK_SIZE];
                int bytesRead;

                int idx = 1;
                boolean isLastChunk;

                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    isLastChunk = (fileInputStream.available() == 0);  // check if is the last chunk
//                    String strIdx = idx + "";
//                    System.out.println(strIdx + ". Sending: " + bytesRead + " bytes");
                    ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);

//                    Future<Void> future = session.getAsyncRemote().sendBinary(byteBuffer, isLastChunk);
                    session.getBasicRemote().sendBinary(byteBuffer,isLastChunk);
//                    future.get();
                    idx++;
                }
            }
            System.out.println("File transfer finished!");
            String fileName = file.getName();
            session.getBasicRemote().sendText(fileName);
//            session.close();
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
