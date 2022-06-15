package com.xy124.auth.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xy124.auth.config.auth.CommonUserDetailsService;
import com.xy124.auth.model.User;
import com.xy124.auth.model.dto.TokenDto;
import com.xy124.auth.model.dto.response.AuthenticationResponse;
import com.xy124.auth.service.IUserService;
import com.xy124.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final IUserService userService;

    private final JwtUtil jwtUtil;

    private final CommonUserDetailsService commonUserDetailsService;

    @PutMapping("token")
    public ResponseEntity<?> createAuthenticationToken(User user) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = commonUserDetailsService.loadUserByUsername(user.getUserId());
        //TODO userDetails이 잘못들어왔을때에 에러페이지 관리 필요
        final TokenDto jwt = jwtUtil.generateToken(userDetails);
        userService.updateUser(user.getUserId(), jwt.getAccessToken());
        return ResponseEntity.ok(new AuthenticationResponse(jwt.getAccessToken()));

    }
    //TODO refreshToken 을 바깥으로 뺀다 .

    @PostMapping("refresh-token")
    public ResponseEntity<?> reGenerateToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;

        TokenDto jwt = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("accessToken"))
                accessToken = cookie.getValue();
        }

        String username = null;
        username = jwtUtil.extractUsername(accessToken);
        UserDetails userDetails = commonUserDetailsService.loadUserByUsername(username);
        DecodedJWT decodedJWT = JWT.decode(accessToken);
        User user = userService.findUserWithPassword(username);

        if (decodedJWT.getExpiresAt().after(new Date())) {
            if (jwtUtil.validToken(user.getRefreshToken(), userDetails)) {
                jwt = jwtUtil.generateToken(userDetails);
            }
        }

        userService.updateUser(username, jwt.getRefreshToken());
        return ResponseEntity.ok(new AuthenticationResponse(jwt.getAccessToken()));
    }


}
