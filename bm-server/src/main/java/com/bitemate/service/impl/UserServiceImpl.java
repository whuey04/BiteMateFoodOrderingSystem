package com.bitemate.service.impl;

import com.bitemate.dto.UserLoginDTO;
import com.bitemate.entity.User;
import com.bitemate.exception.LoginFailedException;
import com.bitemate.mapper.UserMapper;
import com.bitemate.result.Result;
import com.bitemate.service.UserService;
import com.bitemate.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String MyEmail;
    @Resource
    private JavaMailSender javaMailSender;


    @Override
    public Result sendCodeByEmail(User user, HttpServletRequest request) {
        log.info("Start to send email....");

        String email = user.getEmail();

        if(StringUtils.isNotBlank(email)){
            //Generate Code
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = {}", code);

            // Create email object
            SimpleMailMessage message = new SimpleMailMessage();

            // Email Sender
            message.setFrom(MyEmail);
            // Email Receiver
            message.setTo(email);

            // Set Email Content
            message.setSubject("[BiteMate] Verification Code");
            String content = "Thank you for using BiteMate. This is your verification code: " + code + " . Please log in as soon as possible ^-^";
            message.setText(content);

            // Save the generated verification code into the session
            // Store the generated email and verification code in the session.
            // Later, when the user enters the code, we need to handle it with exception catching.
            HttpSession session = request.getSession();
            Map<String, Object> codeInfo = new HashMap<>();
            codeInfo.put("code", code);
            codeInfo.put("expireTime", System.currentTimeMillis() + 3 * 60 * 1000); // Expire in 3 minutes
            session.setAttribute("codeInfo", codeInfo);

            try {
                javaMailSender.send(message);
                return Result.success("Sent successfully");
            }catch (MailException e){
                e.printStackTrace();
            }
        }
        return Result.error("Sending failed");
    }

    /**
     * User Login by Email
     *
     * @param userLoginDTO
     * @param request
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        String email = userLoginDTO.getEmail();
        String code = userLoginDTO.getCode();

        if (StringUtils.isBlank(email) || StringUtils.isBlank(code)) {
            throw new LoginFailedException("Email or verification code cannot be empty");
        }

        HttpSession session = request.getSession();
        Map<String, Object> codeInfo = (Map<String, Object>) session.getAttribute("codeInfo");

        if (codeInfo == null) {
            throw new LoginFailedException("Verification code does not exist or has expired");
        }

        String trueCode = String.valueOf(codeInfo.get("code"));
        Long expireTime = (Long) codeInfo.get("expireTime");

        if(System.currentTimeMillis() > expireTime){
            session.removeAttribute("codeInfo");
            throw new LoginFailedException("Verification code has expired");
        }

        if (!code.trim().equals(trueCode.trim())) {
            throw new LoginFailedException("Verification code is incorrect");
        }

        session.removeAttribute("codeInfo");

        User user = userMapper.checkUser(email);

        if (user == null) {
            user = User.builder()
                    .email(email)
                    .name(email.split("@")[0])
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insertUser(user);
        }

        return user;
    }

}
