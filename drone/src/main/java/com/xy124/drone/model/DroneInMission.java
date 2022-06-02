package com.xy124.drone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "drone_in_mission")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class DroneInMission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="drone_in_mission_seq")
    private Long droneInMissionSeq;

    @OneToOne
    @JoinColumn(name="drone_seq")
    @JsonIgnore
    private Drone drone;

    @ManyToOne
    @JoinColumn(name="mission_seq")
    private Mission mission;


}
