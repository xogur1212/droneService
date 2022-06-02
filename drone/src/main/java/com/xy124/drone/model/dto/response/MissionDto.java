package com.xy124.drone.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xy124.drone.model.Mission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionDto {


    private Long id;
    private String name;
    private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updateDt;
    private int totalDistance;
    private int estimatedTime;

    private List<MissionDetailsDto> missionDetails = new ArrayList<>();


    public MissionDto(Mission mission) {
        if (mission != null) {
            this.id=mission.getMissionSeq();
            this.name = mission.getName();
            this.userId = mission.getUserId();
            this.updateDt = mission.getUpdateDt();
            this.totalDistance = mission.getTotalDistance();
            this.estimatedTime = mission.getEstimatedTime();
            this.missionDetails= mission.getMissionDetails().stream().map(MissionDetailsDto::new).collect(Collectors.toList());
        }

    }
}
