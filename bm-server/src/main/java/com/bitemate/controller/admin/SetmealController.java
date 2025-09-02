package com.bitemate.controller.admin;

import com.bitemate.dto.SetmealDTO;
import com.bitemate.dto.SetmealPageQueryDTO;
import com.bitemate.result.PageResult;
import com.bitemate.result.Result;
import com.bitemate.service.SetmealService;
import com.bitemate.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "Admin - Set Meal Controller")
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * Page query for set meal
     * @param dto
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "Page Query of Set Meal")
    public Result<PageResult> querySetmealByPage(SetmealPageQueryDTO dto) {
        log.info("Set meal Page Query: {}", dto);
        PageResult pageResult = setmealService.pageQuerySetmeal(dto);
        return Result.success(pageResult);
    }

    /**
     * Add set meal
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "Add New Set Meal")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("Add Set meal: {}", setmealDTO);
        setmealService.addSetmealWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * Get set meal by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get Set Meal by ID")
    public Result<SetmealVO> getSetmealById(@PathVariable("id") Long id){
        log.info("Get Set Meal by id: {}", id);
        SetmealVO setmealVO = setmealService.getSetmealByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * Update setmeal
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "Update Set Meal")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("Update Set meal: {}", setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * Update setmeal status
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "Update Status of Set Meal")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result updateSetmealStatus(@PathVariable("status") Integer status, Long id){
        setmealService.updateSetmealStatus(status, id);
        return Result.success();
    }

    /**
     * Delete set meal
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "Delete Set Meal")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result deleteSetmeal(@RequestParam List<Long> ids){
        setmealService.deleteSetmealbyBatch(ids);
        return Result.success();
    }
}
