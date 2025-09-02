package com.bitemate.controller.user;

import com.bitemate.entity.Setmeal;
import com.bitemate.result.Result;
import com.bitemate.service.SetmealService;
import com.bitemate.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/setmeal")
@Api(tags = "User - Set Meal Controller")
public class UserSetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * Get set meal list by category id
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "Get Set Meal List")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId")
    public Result<List<Setmeal>> getSetmealList(Long categoryId) {
        List<Setmeal> list = setmealService.getSetmealList(categoryId);
        return Result.success(list);
    }

    /**
     * Get the dish list included in the set meal
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation(value = "Get Dish List")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }

}
