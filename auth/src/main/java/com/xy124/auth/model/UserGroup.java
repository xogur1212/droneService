package com.xy124.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_group")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_group_seq")
    private Long userGroupSeq;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_desc")
    private String groupDesc;

    @Column(name = "insert_dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp insertDt;

    @Column(name = "insert_user_seq")
    private Long insertUserSeq;

    @Column(name = "update_dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updateDt;

    @Column(name = "update_user_seq")
    private Long updateUserSeq;

    @Column(name = "user_group_status")
    private String userGroupStatus;

    @OneToMany(mappedBy = "userGroup",fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private final List<UserInGroup> userInGroup =new ArrayList<>();

    @OneToMany(mappedBy = "userGroup2", cascade = CascadeType.ALL)
    private final List<UserGroupPermit> userGroupPermit = new ArrayList<>();

//    @OneToOne
//    @JoinColumn(name = "user_group_status", referencedColumnName = "code_value", updatable = false, insertable = false)
//    private UserStatus userStatus;



}
