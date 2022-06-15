package com.xy124.auth.controller;

import com.xy124.auth.model.User;
import com.xy124.auth.model.dto.Result;
import com.xy124.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * @param userId
     * @return result
     * 성공  code  : 1 message :성공 메시지
     * 실패  code  : 0 message :실패 메시지
     */
    public ResponseEntity<?> checkId(@PathVariable String userId) {

        Result result = userService.checkId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

/**
    @GetMapping("/check/authority/{authName}/{permit}")
    public ResponseEntity<?> checkAuthority(@PathVariable String authName, @PathVariable String permit) {
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.checkAuthority(authName, permit));
    }
    */
//추가해야될 내용 위에

    @GetMapping("/{userSeq}")
    public ResponseEntity<?> findOneUser(@PathVariable Long userSeq){


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findOneUser(userSeq));
    }

    @PutMapping
    public ResponseEntity<?> saveUser(@RequestBody User user){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.saveUser(user));
    }



}
