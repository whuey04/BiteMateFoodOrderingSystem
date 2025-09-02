package com.bitemate.service;

import com.bitemate.dto.DishDTO;
import com.bitemate.dto.DishPageQueryDTO;
import com.bitemate.entity.Dish;
import com.bitemate.result.PageResult;
import com.bitemate.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * Page Query for all dish
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQueryDish(DishPageQueryDTO dishPageQueryDTO);

    /**
     * Add new dish with flavor
     *
     * @param dishDTO
     */
    void addDishWithFlavor(DishDTO dishDTO);

    /**
     * Get dish by id
     *
     * @param id
     * @return
     */
    DishVO getDishById(Long id);

    /**
     * Update dish
     *
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * Set dish status
     *
     * @param status
     * @param id
     */
    void updateDishStatus(Integer status, Long id);

    /**
     * Delete Dishes
     * @param ids
     */
    void deleteDishByBatch(List<Long> ids);

    /**
     * Search for dishes by category id
     * @param categoryId
     * @return
     */
    List<Dish> listDish(Long categoryId);

    //User

    /**
     * Get dish list by category id
     * @param categoryId
     * @return
     */
    List<DishVO> getDishlistWithFlavor(Long categoryId);
}
