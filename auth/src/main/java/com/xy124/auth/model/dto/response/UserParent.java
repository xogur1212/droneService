//package com.xy124.auth.model.dto.response;
//
// 정적 팩토리 메소드 패턴
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.xy124.auth.model.User;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.sql.Timestamp;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//public abstract class UserParent {
//
//    private Long userSeq;
//    private String userId; //에러가 날경우 userId-> userName auth 쪽에있는 get set userId userName으로 바꿀 것 (repository도 수정)
//    private String userName; //위에 문제로 바꿨을 경우 이것도 수정
//    private String email;
//    private String tel;
//    private String address;
//    private int status;
//    private String statusName;
//    private String detailAddress;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
//    private Timestamp insertDt;
//    private Long insertUserSeq;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
//    private Timestamp updateDt;
//    private Long updateUserSeq;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
//    private Timestamp lastLoginDt;
//    private String checked;
//    private UserStatusDto userStatus;
//
//    public UserParent(User user) {
//        this.userSeq = user.getUserSeq();
//        this.userId = user.getUserId();
//        this.userName = user.getUserName();
//        this.email = user.getEmail();
//        this.tel = user.getTel();
//        this.address = user.getAddress();
//        this.status = Integer.parseInt(user.getStatus());
//        this.detailAddress = user.getDetailAddress();
//        this.lastLoginDt = user.getLastLoginDt();
//        this.insertUserSeq = user.getInsertUserSeq();
//        this.updateUserSeq = user.getUpdateUserSeq();
//        this.insertDt = user.getInsertDt();
//        this.updateDt = user.getUpdateDt();
//        this.userStatus = new UserStatusDto(user.getUserStatus());
//    }
//
//    public static UserParent from(User user,String returnType){
//        if(returnType.equals("A"))
//            return new UserReturnA(user);
//        else
//            return new UserReturnB(user);
//    }
//
//}
