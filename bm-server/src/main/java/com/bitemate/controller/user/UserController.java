package com.bitemate.controller.user;

import com.bitemate.constant.JwtClaimsConstant;
import com.bitemate.dto.UserLoginDTO;
import com.bitemate.entity.User;
import com.bitemate.properties.JwtProperties;
import com.bitemate.result.Result;
import com.bitemate.service.UserService;
import com.bitemate.utils.JwtUtil;
import com.bitemate.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user/user")
@Api(tags = "User - User Controller")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;


    /**
     * Send Verification Code by Email
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/sendEmail")
    public Result sendCodeByEmail(@RequestBody User user, HttpServletRequest request){
        return userService.sendCodeByEmail(user, request);
    }


    @PostMapping("/login")
    @ApiOperation(value = "User Login By Email")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {

        User user = userService.login(userLoginDTO, request);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.generateJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }
}
