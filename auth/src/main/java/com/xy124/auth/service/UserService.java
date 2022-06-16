package com.xy124.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xy124.auth.config.auth.CommonUserDetails;
import com.xy124.auth.model.User;
import com.xy124.auth.model.dto.Result;
import com.xy124.auth.model.dto.ResultCode;
import com.xy124.auth.model.dto.response.UserResponse;
import com.xy124.auth.repository.UserGroupRepository;
import com.xy124.auth.repository.UserInGroupRepository;
import com.xy124.auth.repository.UserRepository;
import com.xy124.auth.repository.UserStatusRepository;
import com.xy124.auth.util.LoginInfoUtil;
import com.xy124.auth.util.ResultUtil;
import com.xy124.auth.util.SHA256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Map;

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
    public UserResponse findOneUser(Long userSeq) {
        User user = userRepository.findByUserSeq(userSeq);
        return new UserResponse(user);
    }

    @Override
    public Result saveUser(User user) {
        if (user.getUserId() == null || user.getUserName() == null || user.getPassword() == null) {
            //TODO 비밀번호 암호화 해서 저장s
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);

        }

        User findUser = userRepository.findByUserId(user.getUserId());
        if (findUser != null) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        }

        SHA256 sha256 = new SHA256();

        try {
            String cryptoPassword = sha256.encrypt(user.getPassword());
            user.setPassword("{SHA-256}" + cryptoPassword);
        } catch (NoSuchAlgorithmException e) {
            //TODO exception 처리
            e.printStackTrace();
        }
        CommonUserDetails commonUserDetails = LoginInfoUtil.getUserDetails();
        if (commonUserDetails != null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            user.setInsertUserSeq(commonUserDetails.getUserSeq());
            user.setInsertDt(timestamp);
        }


        userRepository.save(user);
        return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
    }

    @Override
    public Result updateUser(Map<String, Object> paramMap) {
        //TODO ObjectMapper 공통으로 묶는 부분 필요
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.convertValue(paramMap, User.class);

        User findUser = userRepository.findByUserSeq(user.getUserSeq());

        if(findUser != null){

            if(user.getPassword() != null){
                SHA256 sha256 = new SHA256();

                try {
                    String cryptoPassword = sha256.encrypt(user.getPassword());
                    findUser.setPassword("{SHA-256}" +cryptoPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
            if (user.getUserName() != null)
                findUser.setUserName(user.getUserName());
            if (user.getEmail() != null)
                findUser.setEmail(user.getEmail());
            if (user.getTel() != null)
                findUser.setTel(user.getTel());
            if (user.getAddress() != null)
                findUser.setAddress(user.getAddress());
            if (user.getStatus() != null)
                findUser.setStatus(user.getStatus());
            if (user.getDetailAddress() != null)
                findUser.setDetailAddress(user.getDetailAddress());

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            findUser.setUpdateDt(timestamp);
            userRepository.save(findUser);


            return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
        }

        return ResultUtil.makeSuccessResult(ResultCode.FAIL);
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
