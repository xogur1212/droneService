package com.xy124.auth.controller;

import com.xy124.auth.model.User;
import com.xy124.auth.model.dto.Result;
import com.xy124.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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


    @GetMapping("/{userSeq}")
    public ResponseEntity<?> findOneUser(@PathVariable Long userSeq) {


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findOneUser(userSeq));
    }

    /**
     * @param user user_status 에 먼저 데이터가 있어야 한다!
     *             {
     *             "userId":"manager92",
     *             "password":"123",
     *             "email":"xogur1212@naver.com",
     *             "tel":"01032707784",
     *             "address":"독산",
     *             "status":0,
     *             "detailAddress":"집",
     *             "insertUserSeq":"5",
     *             "userName":"엄태혁"
     *             }
     * @return 성공 실패에 따른 result code & message
     */
    @PutMapping
    public ResponseEntity<?> saveUser(@RequestBody User user) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.saveUser(user));
    }

    @PatchMapping()
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> paramMap) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUser(paramMap));
    }


}
