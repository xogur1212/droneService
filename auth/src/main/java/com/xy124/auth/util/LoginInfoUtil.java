package com.xy124.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xy124.auth.config.auth.CommonUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginInfoUtil {

    public static CommonUserDetails getUserDetails() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser"))
            return null;
        //TODO objectMapper를 이용한 변환 모듈 작성
        ObjectMapper objectMapper = new ObjectMapper();
        CommonUserDetails commonUserDetails = objectMapper.convertValue(principal, CommonUserDetails.class);
        return commonUserDetails;
    }
}
