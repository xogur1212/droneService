package com.xy124.drone.service;

import com.xy124.drone.model.DroneDetails;
import com.xy124.drone.model.dto.Result;
import com.xy124.drone.model.dto.request.DroneDetailRequest;

public interface IDroneDetailsService {

    DroneDetails saveDroneDetails(DroneDetails droneDetails, Long droneId);

    Result updateDroneDetails(DroneDetailRequest droneDetailRequest);

    DroneDetails findDroneDetails(Long droneDetailsSeq);

    Result updateDroneDetailsFileName(String fileName, long droneSeq);


}
