package com.xy124.drone.service;

import com.xy124.drone.model.DroneBase;
import com.xy124.drone.repository.DroneBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DroneBaseService implements IDroneBaseService{
    private final DroneBaseRepository droneBaseRepository;

    @Override
    public List<DroneBase> findAllDroneBase() {
        return droneBaseRepository.findAll();
    }
}
