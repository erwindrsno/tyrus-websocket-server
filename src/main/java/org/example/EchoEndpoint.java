package org.example;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.concurrent.Future;

@ServerEndpoint(value = "/echo")
public class EchoEndpoint {
    static Logger logger = LoggerFactory.getLogger(App.class);
    final int CHUNK_SIZE = 10240;

    @OnOpen
    public void onOpen(Session session) {
        // System.out.println("Thread : " + Thread.currentThread().getName());
        logger.info("Thread : " + Thread.currentThread().getName());

        File file = new File("T06xxyyy.zip");

        if(!file.exists()){
            logger.error("File not found");
            return;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;

            boolean isLastChunk;

            logger.info("Sending file by chunking (10240 bytes)");
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                isLastChunk = (fileInputStream.available() == 0);  // check if is the last chunk
//                    String strIdx = idx + "";
//                    System.out.println(strIdx + ". Sending: " + bytesRead + " bytes");
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);

                session.getBasicRemote().sendBinary(byteBuffer,isLastChunk);
            }
            logger.info("File sent");
        } catch(Exception e){
            logger.error("error at sending file");
            e.printStackTrace();
        }

        try{
            logger.info("Sending file name and user name");
            String fileName = file.getName();
            UserFile userFile = new UserFile("car",fileName);
            
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(userFile);
            session.getBasicRemote().sendText(json);
            logger.info("File name and user name sent");
        } catch(Exception e){
            logger.error("error at sending file name and user name");
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
        logger.info("From client : " + message);
    }

    @OnClose
    public void onClose(){
        System.out.println("Connection closed.");
    }
}
