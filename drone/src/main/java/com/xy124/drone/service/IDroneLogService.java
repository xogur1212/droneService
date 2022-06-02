package com.xy124.drone.service;

import com.xy124.drone.model.DroneLog;

import java.util.Map;

public interface IDroneLogService {

    DroneLog saveDroneLog(DroneLog droneLog);

    DroneLog findByDroneLogSeq(Long droneLogSeq);

    Map<String, Object> findAllDroneLog(Map<String, Object> paramMap);


}
