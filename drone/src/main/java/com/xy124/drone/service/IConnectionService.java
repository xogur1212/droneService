package com.xy124.drone.service;

import io.dronefleet.mavlink.MavlinkConnection;

import java.net.Socket;
import java.util.Map;

public interface IConnectionService {

    Map<Integer, Map<String, Object>> getSocketList();

    MavlinkConnection connect(Socket socket);

    int isConnected(MavlinkConnection connection);

    void saveMap(int systemId, int index, Map<Integer, Socket> socketList);

}
