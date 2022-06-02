package com.xy124.drone.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "drone_socket")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class DroneSocket {
    @Id
    @Column(name = "drone_socket_seq")
    Long droneSocketSeq;

    String ip;

    String port;

    String localport;
    @Column(name = "system_id")
    int systemId;
}
