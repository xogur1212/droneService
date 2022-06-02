package com.xy124.drone.service;


import com.xy124.drone.manager.MapManager;
import com.xy124.drone.model.DroneSocket;
import com.xy124.drone.socket.CustomServerSocket;
import io.dronefleet.mavlink.MavlinkConnection;
import io.dronefleet.mavlink.MavlinkMessage;
import io.dronefleet.mavlink.common.Heartbeat;
import io.dronefleet.mavlink.common.MavAutopilot;
import io.dronefleet.mavlink.common.MavState;
import io.dronefleet.mavlink.common.MavType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ConnectionService implements IConnectionService{

    private final DroneSocketService droneSocketService;
    private final CustomServerSocket ServerSocket;
    private final MapManager mapManager;
    Map<String, Object> connectionMap = new HashMap<>();
    Map<Integer, Map<String, Object>> systemIdConnectionMap = new HashMap<>();
    Long currentIndex = -1L;

    @Override
    public Map<Integer, Map<String, Object>> getSocketList() {

        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<Integer, Socket> socketList = ServerSocket.getServerThread().getSocketMap();
        int systemId = -1;
        MavlinkConnection connection = null;
        List<Integer> deleteList = new ArrayList<>();
        log.info("socketList={}", socketList);
        if (socketList.isEmpty() || socketList == null)
            return null;


        for (Integer index : socketList.keySet()) {
            log.info("key={}", index);
            Socket socket = socketList.get(index);
            currentIndex = Long.valueOf(index);

            connection = connect(socket);


            log.info("connection={}", connection);

            if ((systemId = isConnected(connection)) != -1) {
                log.info("addMap={}", index);
                saveMap(systemId, index, socketList);


            } else {
                connection = null;
                log.info("deleteList={}", index);
                deleteList.add(index);
            }
            systemId = 0;

        }
        for (Integer deleteIndex : deleteList) {
            ServerSocket.getServerThread().destroySocket(deleteIndex);
        }

        return systemIdConnectionMap;
    }
    @Override
    public MavlinkConnection connect(Socket socket) {

        MavlinkConnection connection = null;

        try {
            connection = MavlinkConnection.create(socket.getInputStream(), socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }
    @Override
    public int isConnected(MavlinkConnection connection) {

        int linkId = 1;
        int systemId = 0;
        long timestamp = System.currentTimeMillis(); /* provide microsecond time */
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
            secretKey = MessageDigest.getInstance("SHA-256").digest("danusys".getBytes(StandardCharsets.UTF_8));
            Heartbeat firstHeartbeat = Heartbeat.builder()
                    .autopilot(MavAutopilot.MAV_AUTOPILOT_GENERIC)
                    .type(MavType.MAV_TYPE_GENERIC)
                    .systemStatus(MavState.MAV_STATE_UNINIT)
                    .baseMode()
                    .mavlinkVersion(3)
                    .build();


            connection.send2(0, 0, firstHeartbeat, linkId, timestamp, secretKey);
            t.schedule(tt, 0, 1000);



            while (timeSec[0] < 3) {


                while ((message = connection.next()) != null) {
                    //TODO socket try catch 안쪽으로 별개로 작성
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
        } catch (IOException e) {
            log.info("IOEEXCEPTION");
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tt.cancel();
            t.cancel();
            timeSec[0] = 0;
            mapManager.addConecctionMap(connection, 0);
        }

        return systemId;


    }

    @Override
    public void saveMap(int systemId, int index, Map<Integer, Socket> socketList) {
        Map<String, Object> map = new HashMap<>();

        map.put("index", index);
        map.put("port", socketList.get(index).getPort());
        map.put("address", socketList.get(index).getInetAddress());
        map.put("localPort", socketList.get(index).getLocalPort());
        log.info("map={}", map);
        systemIdConnectionMap.put(systemId, map);
        DroneSocket droneSocket = new DroneSocket();
        droneSocket.setDroneSocketSeq(Long.valueOf(index));
        droneSocket.setPort(Integer.toString(socketList.get(index).getPort()));
        droneSocket.setIp(socketList.get(index).getInetAddress().toString());
        droneSocket.setLocalport(Integer.toString(socketList.get(index).getLocalPort()));
        droneSocket.setSystemId(systemId);
        droneSocketService.saveList(droneSocket);
        socketList = new HashMap<>();


    }

    public MavlinkConnection getMavlinkConnection(int index) {

        MavlinkConnection connection = (MavlinkConnection) connectionMap.get("connection" + index);
        return connection;
    }

    public Map<String, Object> getConnectionMap() {
        return connectionMap;
    }
}
