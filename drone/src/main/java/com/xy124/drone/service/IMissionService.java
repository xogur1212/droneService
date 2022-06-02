package com.xy124.drone.service;

import com.xy124.drone.model.Mission;
import com.xy124.drone.model.dto.Result;

import java.util.List;
import java.util.Map;

public interface IMissionService {

    List<Mission> findAllMission();

    Mission findOneMission(Long missionSeq);

    List<Mission> findMissionList(Map<String, Object> paramMap);

    Long saveMission(Mission mission);

    int updateMission(Mission mission);

    Result deleteMission(Mission mission);

}
