package com.xy124.drone.repository;

import com.xy124.drone.model.Drone;
import com.xy124.drone.model.DroneInMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DroneInMissionRepository extends JpaRepository<DroneInMission, Long> {

    Optional<DroneInMission> findByDrone(Drone drone);

    int deleteByDrone(Drone drone);

    @Modifying
    @Query(value = "delete from drone_in_mission  where mission_seq=:case_1", nativeQuery = true)
    void deleteDroneInMission(@Param("case_1") long mission_seq);

    @Modifying
    @Query(value = "delete from drone_in_mission  where drone_seq=:case_1", nativeQuery = true)
    void deleteDroneInMissionbySeq(@Param("case_1") long drone_seq);

}
