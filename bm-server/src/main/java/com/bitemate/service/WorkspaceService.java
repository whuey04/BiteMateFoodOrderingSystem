package com.bitemate.service;

import com.bitemate.vo.BusinessDataVO;
import com.bitemate.vo.DishOverViewVO;
import com.bitemate.vo.OrderOverViewVO;
import com.bitemate.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {
    /**
     * Get today business data
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getTodayBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * Get overview of orders
     * @return
     */
    OrderOverViewVO getOrderOverview();

    /**
     * Get overview of dishes
     * @return
     */
    DishOverViewVO getDishOverview();

    /**
     * Get overview of setmeal
     * @return
     */
    SetmealOverViewVO getSetmealOverview();
}
