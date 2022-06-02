package com.xy124.drone.socket;

import com.xy124.drone.model.DroneSocket;
import com.xy124.drone.service.DroneSocketService;
import io.dronefleet.mavlink.MavlinkConnection;
import io.dronefleet.mavlink.MavlinkMessage;
import io.dronefleet.mavlink.common.Heartbeat;
import io.dronefleet.mavlink.common.MavAutopilot;
import io.dronefleet.mavlink.common.MavState;
import io.dronefleet.mavlink.common.MavType;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
public class ServerThread extends Thread {

    private DroneSocketService droneSocketService;
    private ServerSocket serverSocket;
    private Map<Integer, Socket> socketMap = new HashMap<>();
    private Map<Integer, Object> systemIdConnectionMap = new HashMap<>();

    private int count = 1;
    private boolean flag = true;

    public ServerThread(ServerSocket serverSocket, DroneSocketService droneSocketService) {
        this.serverSocket = serverSocket;
        this.droneSocketService = droneSocketService;
    }

    public Map<Integer, Socket> getSocketMap() {
        return socketMap;
    }

    public Map<Integer,Object> getSystemIdConnectionMap(){
        return systemIdConnectionMap;
    }
    
    //TODO EXception 처리
    public boolean destroySocket(int deleteSocketSeq){
        if (deleteSocketSeq != -1) {
            System.out.println("Delete Socket Seq : "+ (deleteSocketSeq));
            Socket socket=socketMap.get(deleteSocketSeq);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socketMap.remove(deleteSocketSeq);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        Socket socket = null;
        List<Integer> deleteList = new ArrayList<>();
        while (flag) {
            try {

                int systemId =0;
                socket = serverSocket.accept();
                System.out.println("Thread " + count + "connected");
                //ClientThread testThread = new ClientThread(socket, count);
                socketMap.put(count, socket);
                log.info("socketMap={}",socketMap);
                MavlinkConnection connection = this.connect(socket,count);
                if(connection != null) {
                    if ((systemId = isConnected(connection)) != -1) {
                        log.info("syststemId={}", systemId);
                        saveMap(systemId, count, socketMap);
                    } else {
                        connection = null;
                        log.info("deleteList={}", count);
                        deleteList.add(count);
                    }
                    for (Integer deleteIndex : deleteList) {
                        this.destroySocket(deleteIndex);
                    }
                }

            } catch (IOException | InterruptedException e) {
                System.out.println("통신소켓 생성불가");
                if (!socket.isClosed()) {
                    try {
                        System.out.println("소켓 삭제");
                        socket.close();
                        socketMap.remove(count);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            socketMap.remove(count);
            count++;
        }
    }
    //TODO Exception 처리 ..
    public MavlinkConnection connect(Socket socket,int count) throws InterruptedException {

        MavlinkConnection connection = null;

        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            Thread.sleep(2000);
            boolean ready = reader.ready();
            log.info("###reader.ready() : {}", ready);

            if (ready) {
                connection = MavlinkConnection.create(socket.getInputStream(), socket.getOutputStream());

            } else {
                this.destroySocket(count);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }
    //TODO Exception 처리 ..
    public int isConnected(MavlinkConnection connection) {

        int linkId = 1;
        int systemId = 0;
        long timestamp = System.currentTimeMillis();/* provide microsecond time */
        MavlinkMessage message;
        byte[] secretKey = new byte[0];
        final int[] timeSec = {0};
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                timeSec[0]++;
            }
        };
        try {
            secretKey = MessageDigest.getInstance("SHA-256").digest("xy124".getBytes(StandardCharsets.UTF_8));
            Heartbeat firstHeartbeat = Heartbeat.builder()
                    .autopilot(MavAutopilot.MAV_AUTOPILOT_GENERIC)
                    .type(MavType.MAV_TYPE_GENERIC)
                    .systemStatus(MavState.MAV_STATE_UNINIT)
                    .baseMode()
                    .mavlinkVersion(3)
                    .build();


            connection.send2(1, 1, firstHeartbeat, linkId, timestamp, secretKey);
            t.schedule(tt, 0, 1000);

            while (timeSec[0] < 3) {
                if((message = connection.next()) != null) {
                    if (timeSec[0] >= 3) {
                        break;
                    }
                    if (message.getPayload() instanceof Heartbeat) {
                        systemId = message.getOriginSystemId();
                        break;
                    }
                }

            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            log.info("EOFException");
            return -1;
        } catch (IOException e) {
            log.info("IOException");
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tt.cancel();
            t.cancel();
            timeSec[0] = 0;

        }

        return systemId;


    }

    public void saveMap(int systemId, int droneSocketSeq, Map<Integer, Socket> socketList) {
        Map<String, Object> map = new HashMap<>();

        map.put("index", droneSocketSeq);
        map.put("socket",socketList.get(droneSocketSeq));
        log.info("map={}", map);
        systemIdConnectionMap.put(systemId, map);
        log.info("systemIdConnectionMap={}",systemIdConnectionMap);
        DroneSocket droneSocket = new DroneSocket();
        droneSocket.setDroneSocketSeq(Long.valueOf(droneSocketSeq));
        droneSocket.setPort(Integer.toString(socketList.get(droneSocketSeq).getPort()));
        droneSocket.setIp(socketList.get(droneSocketSeq).getInetAddress().toString());
        droneSocket.setLocalport(Integer.toString(socketList.get(droneSocketSeq).getLocalPort()));
        droneSocket.setSystemId(systemId);
        droneSocketService.saveList(droneSocket);


    }

}
