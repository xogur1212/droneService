package com.xy124.drone.socket;


import com.xy124.drone.service.DroneSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomServerSocket {


    private ServerSocket serverSocket;
    private ServerThread serverThread;
    private final DroneSocketService droneSocketService;
    public void connectServer(int port) throws IOException{
        try {
            serverSocket = new ServerSocket(port); // 생성자 내부에 bind()가 있고, bind() 내부에 listen() 있음
            serverThread = new ServerThread(serverSocket,droneSocketService);
            serverThread.start();




        } catch (IOException e) {
            log.info("서버 종료..");
            serverSocket.close();
        }
    }
    public ServerThread getServerThread() {return this.serverThread;}
    public ServerSocket getServerSocket() {return this.serverSocket;}
}
