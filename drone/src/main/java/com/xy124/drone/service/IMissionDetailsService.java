package com.xy124.drone.service;

import com.xy124.drone.model.MissionDetails;
import com.xy124.drone.model.dto.Result;

import java.util.List;
import java.util.Map;

public interface IMissionDetailsService {

    Result saveMission(List<MissionDetails> missionDetails, long mission_id, double Distance);

    Result updateMissionDetails(List<MissionDetails> missionDetails, long mission_id);

    List<MissionDetails> findMissionDetailsList(Map<String, Object> paramMap);

}
