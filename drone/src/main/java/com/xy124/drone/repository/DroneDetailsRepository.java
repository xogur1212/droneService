package com.xy124.drone.repository;

import com.xy124.drone.model.DroneDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneDetailsRepository extends JpaRepository<DroneDetails, Long> {

    DroneDetails findByDroneDetailsSeq(Long droneDetailsSeq);
}
