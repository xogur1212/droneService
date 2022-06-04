//package com.xy124.drone.config;
//
//import com.xy124.drone.socket.CustomServerSocket;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class ServerSocketConfig implements ApplicationRunner {
//
//    private final CustomServerSocket customServerSocket;
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        customServerSocket.connectServer(8089);
//        log.info("TCP SERVER ON");
//    }
//}
//
