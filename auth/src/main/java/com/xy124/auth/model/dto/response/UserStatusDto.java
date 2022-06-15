package com.xy124.auth.model.dto.response;

import com.xy124.auth.model.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusDto {
    private String codeId;
    private String codeName;
    private int codeValue;

    public UserStatusDto(UserStatus userStatus) {
        this.codeName = userStatus.getCodeName();
        this.codeValue = Integer.parseInt(userStatus.getCodeValue());
    }
}
