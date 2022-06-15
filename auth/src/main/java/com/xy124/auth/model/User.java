package com.xy124.auth.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;


    @Column(name = "id")
    private String userId;

    private String password;

    @Column(name = "user_name")
    private String userName;

    private String email;

    private String tel;

    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "last_login_dt")
    private Timestamp lastLoginDt;

    @Column(name = "insert_user_seq")
    private Long insertUserSeq;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "insert_dt")
    private Timestamp insertDt;

    @Column(name = "update_user_seq")
    private Long updateUserSeq;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "update_dt")
    private Timestamp updateDt;

    private String status;
    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "status", referencedColumnName = "code_value", insertable = false, updatable = false)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserInGroup> userInGroup = new ArrayList<>();

}
