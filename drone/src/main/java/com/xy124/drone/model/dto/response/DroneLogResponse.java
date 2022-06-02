package com.xy124.drone.model.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xy124.drone.model.DroneLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneLogResponse {


    private Long id;
    private String droneDeviceName;
    private String missionName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp insertDt;
    private List<DroneLogDetailsResponse> droneLogDetails;

    public DroneLogResponse(DroneLog droneLog){
        this.id=droneLog.getDroneLogSeq();
        this.droneDeviceName=droneLog.getDroneDeviceName();
        this.missionName=droneLog.getMissionName();
        this.insertDt=droneLog.getInsertDt();
        this.droneLogDetails=droneLog.getDroneLogDetails().stream().map(DroneLogDetailsResponse::new)
                .collect(Collectors.toList());
    }
}
