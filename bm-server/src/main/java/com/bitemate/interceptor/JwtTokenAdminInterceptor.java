package com.bitemate.interceptor;

import com.bitemate.constant.JwtClaimsConstant;
import com.bitemate.context.BaseContext;
import com.bitemate.properties.JwtProperties;
import com.bitemate.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor for the JWT TOKEN Validator
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Valid JWT
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if the intercepted handler is a Controller method or another type of resource
        if (!(handler instanceof HandlerMethod)) {
            // The current request is not targeting a controller method (e.g., it's a static resource), allow it through
            return true;
        }

        //1. Get the token name from request Header
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2. Valid token
        try {
            log.info("token validation:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("Current Emp ID:{}", empId);
            BaseContext.setCurrentId(empId);
            // 3. Token is valid, allow the request to proceed
            return true;
        } catch (Exception e) {
            // 4. Token is invalid, respond with 401 Unauthorized status
            response.setStatus(401);
            return false;
        }
    }
}
