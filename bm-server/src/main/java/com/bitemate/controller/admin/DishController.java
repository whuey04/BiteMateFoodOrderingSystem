package com.bitemate.controller.admin;

import com.bitemate.dto.DishDTO;
import com.bitemate.dto.DishPageQueryDTO;
import com.bitemate.entity.Dish;
import com.bitemate.result.PageResult;
import com.bitemate.result.Result;
import com.bitemate.service.DishService;
import com.bitemate.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "Admin - Dish Controller")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Page Query for all dish
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "Page Query for Dishes")
    public Result<PageResult> queryDishByPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("Display Dish");
        PageResult result = dishService.pageQueryDish(dishPageQueryDTO);
        return Result.success(result);
    }

    /**
     * Add New Dish
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "Add New Dish")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("Add Dish: {}", dishDTO);
        dishService.addDishWithFlavor(dishDTO);

        //clean cache
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }

    /**
     * Get dish by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get Dish by ID")
    public Result<DishVO> getDishById(@PathVariable("id") Long id) {
        log.info("Get Dish by id: {}", id);
        DishVO dish = dishService.getDishById(id);
        return Result.success(dish);
    }

    /**
     * Update dish
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "Update Dish")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("Update Dish: {}", dishDTO);
        dishService.updateDish(dishDTO);

        //clear all the cache
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * Update Dish status
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "Update Status of Dish")
    public Result updateDishStatus(@PathVariable("status") Integer status, Long id) {
        log.info("Update Dish =  status: {}, id: {}", status, id);
        dishService.updateDishStatus(status,id);

        //clear all the cache
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * Delete Dish
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "Delete Dish")
    public Result deleteDish(@RequestParam List<Long> ids) {
        log.info("Delete Dish: {}", ids);
        dishService.deleteDishByBatch(ids);

        //clear all the cache
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * Search for dishes by category id
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "Get Dish List by Category ID")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("List Dish");
        List<Dish> list = dishService.listDish(categoryId);
        return Result.success(list);
    }

    /**
     * Clean the cache data in redis
     * @param pattern
     */
    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
