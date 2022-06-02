package com.xy124.drone.service;

import com.xy124.drone.model.DroneSocket;

import java.util.List;

public interface IDroneSocketService {

    List<DroneSocket> getList();

    void saveList(DroneSocket droneSocket);

    void delete();
}
