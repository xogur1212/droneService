package com.xy124.auth.model;


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="user_status")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class UserStatus {

    @Id
    @Column(name ="code_id")
    private String codeId;

    @Column(name="code_name")
    private String codeName;

    @Column(name="code_value")
    private String codeValue;
}
