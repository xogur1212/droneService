package com.xy124.auth.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "permit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class Permit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="permit_seq")
    private Long permitSeq;

    @Column(name = "code_id")
    private String codeId;

    @Column(name = "code_name")
    private String codeName;

    @Column(name = "code_value")
    private String codeValue;

}
