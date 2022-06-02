package com.xy124.drone.repository;

import com.xy124.drone.model.DroneInMission;
import com.xy124.drone.model.Mission;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Mission> findByMissionSeq(Long missionSeq);

    List<Mission> findAllByNameLikeAndDroneId(String name, int droneId, Sort sort);

    List<Mission> findAllByNameLikeAndDroneIdNot(String name, int droneId, Sort sort);

    List<Mission> findAllByNameLike(String name, Sort sort);

    Mission findByName(String name);

    List<Mission> findAllByUserIdLikeAndDroneInMission(String adminUserId, DroneInMission drone, Sort sort);

    List<Mission> findAllByUserIdLikeAndDroneInMissionNot(String adminUserId, DroneInMission drone, Sort sort);

    List<Mission> findAllByUserIdLike(String adminUserId, Sort sort);

    Long deleteByMissionSeq(Long missionSeq);
}
