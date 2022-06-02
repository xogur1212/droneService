package com.xy124.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name ="group_permit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class UserGroupPermit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_group_permit_seq")
    private Long userGroupPermitSeq;

    @Column(name = "insert_dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp insertDt;
    @Column(name = "insert_user_seq")
    private Long insertUserSeq;

    @Column(name = "user_group_seq")
    private Long userGroupSeq;
    @Column(name = "permit_seq")
    private Long permitSeq;
    @Column(name = "permit_menu_seq")
    private Long permitMenuSeq;
    
    @ManyToOne
    @JoinColumn(name="user_group_seq", updatable = false, insertable = false)
    @JsonIgnore
    private UserGroup userGroup2;


    //Todo 여기서 pk 참조하는데 굳이 refrencedColumnName 쓴이유 ?
    @OneToOne
    @JoinColumn(name="permit_seq", referencedColumnName = "permit_seq",updatable = false,insertable = false)
    private Permit permit;

    @OneToOne
    @JoinColumn(name="permit_menu_seq", referencedColumnName = "permit_menu_seq",updatable = false,insertable = false)
    private PermitMenu permitMenu;

}
