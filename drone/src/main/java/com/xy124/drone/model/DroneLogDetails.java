package com.xy124.drone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "drone_log_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class DroneLogDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="drone_log_details_seq")
    private Long droneLogDetailsSeq;

    @Column(name = "from_target")
    private String fromTarget;

    @Column(name = "to_target")
    private String toTarget;

    private String type;

    private String param1;

    private String param2;

    private String param3;

    private String param4;

    private String param5;

    private String param6;

    private String param7;

    @Column(name ="insert_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp insertTime;

    @ManyToOne
    @JoinColumn(name ="drone_log_id")
    @JsonBackReference
    private DroneLog droneLog;


}
