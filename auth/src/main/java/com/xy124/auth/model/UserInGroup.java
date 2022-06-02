package com.xy124.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name ="user_in_group")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class UserInGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_in_group_seq")
    private Long userInGroupSeq;

    @Column(name="insert_dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp insertDt;

    @Column(name="insert_user_seq")
    private Long insertUserSeq;

    @ManyToOne
    @JoinColumn(name="user_seq")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name ="user_group_seq")
    private UserGroup userGroup;


}
