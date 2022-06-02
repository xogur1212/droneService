package com.xy124.drone.repository;


import com.xy124.drone.model.Drone;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone,Long> {

    Drone findByDroneDeviceName(String droneDeviceName);

    List<Drone> findAllByDroneSeqNot(long id);

    Optional<Drone> findByDroneSeq(Long droneSeq);

    int deleteByDroneSeq(Long droneSeq);

    List<Drone> findAllByUserIdLikeAndStatus(String s, String droneStatus, Sort sort);

    List<Drone> findAllByDroneDeviceNameLikeAndStatus(String s, String droneStatus, Sort sort);

    List<Drone> findAllByUserIdLike(String s, Sort sort);

    List<Drone> findAllByDroneDeviceNameLike(String s, Sort sort);
}
