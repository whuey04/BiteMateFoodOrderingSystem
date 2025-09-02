package com.bitemate.service;

import com.bitemate.dto.UserLoginDTO;
import com.bitemate.entity.User;
import com.bitemate.result.Result;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    /**
     * Send Code By Email
     * @param user
     * @param request
     * @return
     */
    Result sendCodeByEmail(User user, HttpServletRequest request);

    /**
     * User Login by Email
     * @param userLoginDTO
     * @param request
     * @return
     */
    User login(UserLoginDTO userLoginDTO, HttpServletRequest request);
}
