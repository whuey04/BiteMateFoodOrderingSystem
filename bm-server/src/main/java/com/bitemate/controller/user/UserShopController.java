package com.bitemate.controller.user;

import com.bitemate.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/shop")
@Api(tags = "User - Shop Controller")
@Slf4j
public class UserShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Get shop status in user side
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "Get Shop Status")
    public Result<Integer> getShopStatusInUserSide() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("Get the shop's status to: {}", status == 1 ? "Open" : "Closed");
        return Result.success(status);
    }
}
