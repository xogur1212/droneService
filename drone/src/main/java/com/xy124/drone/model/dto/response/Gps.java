package com.xy124.drone.model.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Gps {
    private Double gpsX;  //z
    private Double gpsY;  //y
    private Double currentHeight; // 높이
    private int wpDist; // 목표까지의거리
    private String missionType; //미션 종류 (귀환,이륙,이동 등등)
    private int heading; //방향 0이 north
    private float airSpeed;
    private String sec;
    private String min;
    private String hour;
    private int droneId;
    private int status;
  //  private float airspeed; //수직속도
 //   private float groundspeed;//수평속도





}
