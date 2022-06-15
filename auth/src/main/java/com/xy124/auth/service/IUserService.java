package com.xy124.auth.service;

import com.xy124.auth.model.User;
import com.xy124.auth.model.dto.Result;
import com.xy124.auth.model.dto.response.UserResponse;

public interface IUserService {

    UserResponse findUser(String userName);

    UserResponse findUser(Long userSeq);

    User updateUser(String userName, String token);

    Result checkId(String userId);


    User findUserWithPassword(String username);

    User findOneUser(Long userSeq);

    Result saveUser(User user);
}
