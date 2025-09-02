package com.bitemate.controller.user;

import com.bitemate.result.Result;
import com.bitemate.service.DishService;
import com.bitemate.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "User - Dish Controller")
public class UserDishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Get dish list by category id
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "Get Dish List")
    public Result<List<DishVO>> getDishList(Long categoryId) {

        //Generate the key in redis. Rule : dish_[categoryId]
        String key = "dish_"+categoryId;

        //Check if there is any dish data in Redis.
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() > 0) {
            return Result.success(list);
        }

        //No data. Query the database and save data into redis
        list = dishService.getDishlistWithFlavor(categoryId);
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }
}
