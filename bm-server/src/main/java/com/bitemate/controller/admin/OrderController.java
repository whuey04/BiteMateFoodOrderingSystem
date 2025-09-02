package com.bitemate.controller.admin;

import com.bitemate.dto.OrdersCancelDTO;
import com.bitemate.dto.OrdersConfirmDTO;
import com.bitemate.dto.OrdersPageQueryDTO;
import com.bitemate.dto.OrdersRejectionDTO;
import com.bitemate.result.PageResult;
import com.bitemate.result.Result;
import com.bitemate.service.OrderService;
import com.bitemate.vo.OrderStatisticsVO;
import com.bitemate.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "Admin - Order Controller")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Search order
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation(value = "Search Order")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.orderConditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "Get Order Statistics")
    public Result<OrderStatisticsVO> orderStatistics(){
        OrderStatisticsVO orderStatisticsVO = orderService.getOrderStatistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * Get order details
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation(value = "Get Order Detail")
    public Result<OrderVO> getOrderDetail(@PathVariable("id") Long id){
        OrderVO orderDetails = orderService.getOrderDetails(id);
        return Result.success(orderDetails);
    }

    /**
     * Confirm order
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation(value = "Confirm Order")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * Reject order
     * @param ordersRejectionDTO
     * @return
     * @throws Exception
     */
    @PutMapping("/rejection")
    @ApiOperation(value = "Reject Order")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception{
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * Cancel order by admin
     * @param ordersCancelDTO
     * @return
     * @throws Exception
     */
    @PutMapping("/cancel")
    @ApiOperation(value = "Cancel Order by Admin")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * Delivery Order
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation(value = "Delivery Order")
    public Result deliveryOrder(@PathVariable("id") Long id){
        orderService.deliveryOrder(id);
        return Result.success();
    }

    /**
     * Complete order
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation(value = "Complete Order")
    public Result completeOrder(@PathVariable("id") Long id){
        orderService.completeOrder(id);
        return Result.success();
    }
}
