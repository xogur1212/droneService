package com.xy124.auth.service;

import com.xy124.auth.model.User;
import com.xy124.auth.model.dto.Result;
import com.xy124.auth.model.dto.ResultCode;
import com.xy124.auth.model.dto.response.UserResponse;
import com.xy124.auth.repository.UserGroupRepository;
import com.xy124.auth.repository.UserInGroupRepository;
import com.xy124.auth.repository.UserRepository;
import com.xy124.auth.repository.UserStatusRepository;
import com.xy124.auth.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor(staticName = "from")
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserInGroupRepository userInGroupRepository;
    private final UserStatusRepository userStatusRepository;


    @Override
    public UserResponse findUser(String userName) {
        User user = userRepository.findByUserId(userName);
        return new UserResponse(user);
    }

    @Override
    public UserResponse findUser(Long userSeq) {
        return null;
    }

    @Override
    public User findUserWithPassword(String userName) {
        User user = userRepository.findByUserId(userName);
        return user;
    }

    @Override
    public User findOneUser(Long userSeq) {
        User user = userRepository.findByUserSeq(userSeq);
        return user;
    }

    @Override
    public Result saveUser(User user) {
        if (user.getUserId() != null && user.getUserName() != null && user.getPassword() != null) {
            //TODO 비밀번호 암호화 해서 저장
            userRepository.save(user);
            return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
        } else {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        }

    }

    @Override
    public User updateUser(String userName, String token) {
        User findUser = userRepository.findByUserId(userName);
        if (findUser != null) {
            findUser.setRefreshToken(token);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            findUser.setLastLoginDt(timestamp);
        }
        return findUser;
    }

    @Override
    public Result checkId(String userId) {
        User checkUser = userRepository.findByUserId(userId);
        Result result = new Result();
        if (checkUser == null) {
            result.setCode(1);
            result.setMessage("중복된 아이디가 없습니다");
        } else {
            result.setCode(0);
            result.setMessage("중복된 아이디가 있습니다");
        }
        return result;
    }
}
