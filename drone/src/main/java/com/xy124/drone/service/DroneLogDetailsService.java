package com.xy124.drone.service;

import com.xy124.drone.model.DroneLogDetails;
import com.xy124.drone.repository.DroneLogDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DroneLogDetailsService implements IDroneLogDetailsService {

    private final DroneLogDetailsRepository droneLogDetailsRepository;

    @Override
    public DroneLogDetails saveDroneLogDetails(DroneLogDetails droneLogDetails) {


        return droneLogDetailsRepository.save(droneLogDetails);
    }


}
