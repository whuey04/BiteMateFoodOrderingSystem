package com.bitemate.service.impl;

import com.bitemate.constant.StatusConstant;
import com.bitemate.entity.Orders;
import com.bitemate.mapper.DishMapper;
import com.bitemate.mapper.OrderMapper;
import com.bitemate.mapper.SetmealMapper;
import com.bitemate.mapper.UserMapper;
import com.bitemate.service.WorkspaceService;
import com.bitemate.vo.BusinessDataVO;
import com.bitemate.vo.DishOverViewVO;
import com.bitemate.vo.OrderOverViewVO;
import com.bitemate.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * Get today business data
     *
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getTodayBusinessData(LocalDateTime begin, LocalDateTime end) {
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);

        //Get total num of order
        Integer totalOrderCount = orderMapper.countOrderByMap(map);


        map.put("status", Orders.COMPLETED);
        //Get turnover
        Double turnover = orderMapper.sumStatistics(map);
        turnover = turnover == null ? 0.0 : turnover;

        //valid order
        Integer validOrderCount = orderMapper.countOrderByMap(map);

        Double unitPrice = 0.0;
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){

            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;

            unitPrice = turnover / validOrderCount;
        }

        //新增用户数
        Integer newUsers = userMapper.countUserByMap(map);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }

    /**
     * Get overview of orders
     *
     * @return
     */
    public OrderOverViewVO getOrderOverview() {
        Map map = new HashMap();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));

        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrdersCount = orderMapper.countOrderByMap(map);

        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrdersCount = orderMapper.countOrderByMap(map);

        map.put("status", Orders.COMPLETED);
        Integer completedOrdersCount = orderMapper.countOrderByMap(map);

        map.put("status", Orders.CANCELLED);
        Integer cancelledOrdersCount = orderMapper.countOrderByMap(map);

        map.put("status", null);
        Integer allOrdersCount = orderMapper.countOrderByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrdersCount)
                .deliveredOrders(deliveredOrdersCount)
                .completedOrders(completedOrdersCount)
                .cancelledOrders(cancelledOrdersCount)
                .allOrders(allOrdersCount)
                .build();

    }

    /**
     * Get overview of dishes
     *
     * @return
     */
    public DishOverViewVO getDishOverview() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * Get overview of setmeal
     *
     * @return
     */
    public SetmealOverViewVO getSetmealOverview() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
