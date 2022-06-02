package com.xy124.drone.service;

import com.xy124.drone.model.Drone;
import com.xy124.drone.model.dto.Result;
import com.xy124.drone.model.dto.request.DroneRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IDroneService {

    Result updateDrone(Drone drone);

    Result changeStatus(Drone drone, int armStatus);

    Result saveDrone(Drone drone);

    Result deleteDrone(Drone drone);

    List<?> findDroneList(DroneRequest droneRequest);

    Sort sortByUpdateDt();

    Drone findDrone(Drone drone);

    //error시 response 작성
    Drone findOneDrone(Long droneId);

    List<Drone> findAllDrone();

    Object getSocketDrone();

}
