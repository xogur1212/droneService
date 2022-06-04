package com.xy124.drone.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "drone")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
@Builder
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drone_seq")
    private Long droneSeq;

    @Column(name = "drone_device_name", unique = true, nullable = false)
    private String droneDeviceName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "socket_index", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long socketIndex;

    @Column(name = "update_dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updateDt;

    @Column(name = "arm_status")
    private int armStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "drone_details_id")
    private DroneDetails droneDetails;

    private String status;

    @OneToOne(mappedBy = "drone", cascade = CascadeType.ALL)
    private DroneInMission droneInMission;

    @ManyToOne
    @JoinColumn(name = "drone_base_id")
    private DroneBase droneBase;

}
