package com.xy124.auth.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${defaultFailureUrl}")
    private String defaultFailureUrl;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String defaultFailurlUrl = this.defaultFailureUrl;
        log.trace("error type ={}", authException.getClass().getName());

        String exception = null;
        exception = (String) request.getAttribute("exception");
        log.info("log :exception : {}", exception);

        if (exception != null) {
            if (exception.equals("ExpiredJwtException")) {
                log.error("ExpiredJwtException");
                request.setAttribute("failMsg", "토큰 만료");
            }
        }

        if (authException instanceof AuthenticationServiceException) {
            log.error("AuthenticationServiceException");

            request.setAttribute("failMsg", "가입 되지 않은 사용자");

        } else if (authException instanceof BadCredentialsException) {
            log.error("BadCredentialsException");
            request.setAttribute("failMsg", "아이디 또는 비밀번호가 틀렸습니다.");

        } else if (authException instanceof LockedException) {
            log.error("LockedException");
            request.setAttribute("failMsg", "계정이 잠겨있습니다. 관리자에게 문의하십시오.");

        } else if (authException instanceof DisabledException) {
            log.error("DisabledException");
            request.setAttribute("failMsg", "비황성화된 계정입니다.");

        } else if (authException instanceof AccountExpiredException) {
            log.error("AccountExpiredException");
            request.setAttribute("failMsg", "기간 만료된 계정입니다.");

        } else if (authException instanceof CredentialsExpiredException) {
            log.error("CredentialsExpiredException");
            request.setAttribute("failMsg", "비밀번호 틀렸습니다.");

        } else if (authException instanceof InsufficientAuthenticationException) {
            log.error("InsufficientAuthenticationException");

            request.setAttribute("failMsg", "인증정보가 부족합니다.");

        }

        log.trace("# defaultFailureUrl : {}",defaultFailurlUrl);

        response.sendRedirect(defaultFailurlUrl);

    }
}
