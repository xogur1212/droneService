package com.xy124.drone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "mission_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class MissionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="mission_details_seq")
    private Long missionDetailsSeq;

    @Column(nullable = false)
    private String name;

    private int index;

    @Column(name = "gps_x")
    private double gpsX;

    @Column(name = "gps_y")
    private double gpsY;

    private int alt;

    private int speed;

    private int time;

    private double yaw;

    private int radius;

    @Column(name="ko_name")
    private String koName;

    @ManyToOne
    @JoinColumn(name="mission_id")
    @JsonIgnore
    private Mission mission;


}
