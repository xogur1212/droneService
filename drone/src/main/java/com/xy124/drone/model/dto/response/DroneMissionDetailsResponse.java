package com.xy124.drone.model.dto.response;

import com.xy124.drone.model.Drone;
import com.xy124.drone.model.Mission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DroneMissionDetailsResponse {
    private Long id;
    private String droneDeviceName;
    private MissionDto mission;
    private String droneBase;

    //private List<MissionDetailResponse> missionDetailResponseList;


    public DroneMissionDetailsResponse(Drone drone) {
        this.id = drone.getDroneSeq();
        this.droneDeviceName = drone.getDroneDeviceName();
        if (drone.getDroneInMission() != null) {
            this.mission = new MissionDto(drone.getDroneInMission().getMission());
        }else{
            Mission nullMission=new Mission();
            nullMission.setMissionSeq(null);
            nullMission.setName(null);
            nullMission.setUpdateDt(null);
            nullMission.setTotalDistance(0);
            nullMission.setEstimatedTime(0);

            this.mission =new MissionDto(nullMission);
        }

        this.droneBase = drone.getDroneBase().getBaseName();

    }

}
