package com.bitemate.service;

import com.bitemate.dto.SetmealDTO;
import com.bitemate.dto.SetmealPageQueryDTO;
import com.bitemate.entity.Setmeal;
import com.bitemate.result.PageResult;
import com.bitemate.vo.DishItemVO;
import com.bitemate.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * Page Query for set meal
     * @param dto
     * @return
     */
    PageResult pageQuerySetmeal(SetmealPageQueryDTO dto);

    /**
     * Add set meal
     * @param setmealDTO
     */
    void addSetmealWithDish(SetmealDTO setmealDTO);

    /**
     * Get set meal by id
     * @param id
     * @return
     */
    SetmealVO getSetmealByIdWithDish(Long id);

    /**
     * Update set meal
     * @param setmealDTO
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * Update set meal status
     * @param status
     * @param id
     */
    void updateSetmealStatus(Integer status, Long id);

    /**
     * Delete set meal
     * @param ids
     */
    void deleteSetmealbyBatch(List<Long> ids);

    /**
     * Get set meal list by category id
     * @param categoryId
     * @return
     */
    List<Setmeal> getSetmealList(Long categoryId);

    /**
     * Get the dish list included in the set meal
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
