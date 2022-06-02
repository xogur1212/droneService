package com.xy124.drone.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "drone_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class DroneDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="drone_details_seq")
    private Long droneDetailsSeq;

    private String size;
    private float weight;

    @Column(name="maximum_operating_distance")
    private float maximumOperatingDistance;

    @Column(name="operating_temperature_range_min")
    private float operationTemperatureRangeMin;

    @Column(name="operating_temperature_range_max")
    private float operationTemperatureRangeMax;

    @Column(name="sim_number")
    private String simNumber;

    @Column(name="master_manager")
    private String masterManager;

    @Column(name="sub_manager")
    private String subManager;

    @Column(name="manufacturer")
    private String manufacturer;

    @Column(name="type")
    private String type;

    @Column(name="maximum_management_altitude")
    private int maximumManagementAltitude;

    @Column(name="maximum_operating_speed")
    private int maximumOperatingSpeed;

    @Column(name="maximum_speed")
    private int maximumSpeed;

    @Column(name="insert_user_id")
    private Long insertUserId;

    @Column(name="insert_dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp insertDt;

    @Column(name="update_user_id")
    private Long updateUserId;

    @Column(name="update_dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updateDt;

    @Column(name="thumbnail_img")
    private String thumbnailImg;

    @Column(name="thumbnail_real_img")
    private String thumbnailRealImg;


    private int size1;
    private int size2;
    private int size3;
    @Column(name="maximum_operating_weight")
    private int maximumOperatingWeight;

    @Column(name="flight_time")
    private int flightTime;




}
