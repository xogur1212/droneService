package com.xy124.drone.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneRequest {

    private String droneDeviceName;
    private String userId;
    private String droneStatus;
}
