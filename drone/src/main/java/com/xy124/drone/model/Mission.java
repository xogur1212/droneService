package com.xy124.drone.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mission")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="mission_seq")
    private Long missionSeq;

    @Column(nullable = false)
    private String name;

    @Column(name="user_id")
    private String userId;

    @Column(name = "drone_id")
    private int droneId;

    @Column(name="update_dt")
    @JsonFormat(shape =JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd kk:mm:ss", timezone="Asia/Seoul")
    private Timestamp updateDt;

    @Column(name="total_distance")
    private int totalDistance;

    @Column(name="estimated_time")
    private int estimatedTime;

    @OneToMany(mappedBy = "mission", fetch = FetchType.EAGER)
    private final List<MissionDetails> missionDetails = new ArrayList<>();


    @OneToMany(mappedBy = "mission")
    @JsonIgnore
    private final List<DroneInMission> droneInMission =new ArrayList<>();
}
