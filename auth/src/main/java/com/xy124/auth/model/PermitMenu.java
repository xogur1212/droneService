package com.xy124.auth.model;


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "permit_menu")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class PermitMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="permit_menu_seq")
    private Long permitMenuSeq;


    @Column(name = "code_id")
    private String codeId;

    @Column(name = "code_name")
    private String codeName;

    @Column(name = "code_value")
    private String codeValue;

}
