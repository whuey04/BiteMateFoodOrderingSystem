package com.bitemate.controller.admin;

import com.bitemate.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Api(tags = "Admin - Shop Controller")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Set Shop status
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation(value = "Set Status of Shop")
    public Result setShopStatus(@PathVariable("status") Integer status) {
        log.info("Set the shop's status to: {}", status == 1 ? "Open" : "Closed");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    /**
     * Get shop status in admin side
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "Get Status of Shop")
    public Result<Integer> getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("Get the shop's status to: {}", status == 1 ? "Open" : "Closed");
        return Result.success(status);
    }
}
