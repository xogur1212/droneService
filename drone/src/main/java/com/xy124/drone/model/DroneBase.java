package com.xy124.drone.model;


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name ="drone_base")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class DroneBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="drone_base_seq")
    private Long droneBaseSeq;

    @Column(name="base_name")
    private String baseName;


}
