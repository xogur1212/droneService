package com.xy124.auth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xy124.auth.model.User;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse  {

    private Long userSeq;
    private String userId; //에러가 날경우 userId-> userName auth 쪽에있는 get set userId userName으로 바꿀 것 (repository도 수정)
    private String userName; //위에 문제로 바꿨을 경우 이것도 수정
    private String email;
    private String tel;
    private String address;
    private int status;
    private String statusName;
    private String detailAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp insertDt;
    private Long insertUserSeq;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updateDt;
    private Long updateUserSeq;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
    private Timestamp lastLoginDt;
    private String checked;
    private UserStatusDto userStatus;




    //builder를 사용하면 따로 값 설정을 안한부분으로 해결이 response 대안이 된다 .
    public UserResponse(User user) {
        this.userSeq = user.getUserSeq();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.tel = user.getTel();
        this.address = user.getAddress();
        this.status = Integer.parseInt(user.getStatus());
        this.detailAddress = user.getDetailAddress();
        this.lastLoginDt = user.getLastLoginDt();
        this.insertUserSeq = user.getInsertUserSeq();
        this.updateUserSeq = user.getUpdateUserSeq();
        this.insertDt = user.getInsertDt();
        this.updateDt = user.getUpdateDt();
        this.userStatus = new UserStatusDto(user.getUserStatus());
    }
    public UserResponse(User user, boolean inGroup) {
        this.userSeq = user.getUserSeq();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.tel = user.getTel();
        this.address = user.getAddress();
        this.status = Integer.parseInt(user.getStatus());
        this.detailAddress = user.getDetailAddress();
        this.lastLoginDt = user.getLastLoginDt();
        this.insertUserSeq = user.getInsertUserSeq();
        this.updateUserSeq = user.getUpdateUserSeq();
        this.insertDt = user.getInsertDt();
        this.updateDt = user.getUpdateDt();
        this.checked = inGroup ? "checked" : "unchecked";
        this.userStatus = new UserStatusDto(user.getUserStatus());
    }


}
