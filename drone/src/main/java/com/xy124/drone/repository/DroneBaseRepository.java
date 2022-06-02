package com.xy124.drone.repository;

import com.xy124.drone.model.DroneBase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroneBaseRepository extends JpaRepository<DroneBase, Long> {

    Optional<DroneBase> findByDroneBaseSeq(Long droneBaseSeq);
}
