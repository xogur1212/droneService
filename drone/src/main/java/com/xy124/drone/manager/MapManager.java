package com.xy124.drone.manager;

import com.xy124.drone.util.Flight;
import io.dronefleet.mavlink.MavlinkConnection;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapManager {


    private Map<Integer, Flight> flightMap = new HashMap<>();

    private Map<Integer, Integer> loggingMap = new HashMap<>();

    public Map<Integer,Flight> getFlightMap(){
        return this.flightMap;
    }

    public Map<Integer,Integer> getLoggingMap(){
        return this.loggingMap;
    }



    private Map<MavlinkConnection,Integer> connectionMap =new HashMap<>();

    public void addConecctionMap(MavlinkConnection connection, int isConnected){
        connectionMap.put(connection,isConnected);
    }

    public Map<MavlinkConnection,Integer> getConnectionMap(){
        return this.connectionMap;
    }

}
