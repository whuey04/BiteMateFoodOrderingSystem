package com.bitemate.controller.user;

import com.bitemate.dto.OrdersPaymentDTO;
import com.bitemate.dto.OrdersSubmitDTO;
import com.bitemate.result.PageResult;
import com.bitemate.result.Result;
import com.bitemate.service.OrderService;
import com.bitemate.vo.OrderPaymentVO;
import com.bitemate.vo.OrderSubmitVO;
import com.bitemate.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "User - Order Controller")
public class UserOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Place order
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation(value = "Place Order")
    public Result<OrderSubmitVO> placeOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        OrderSubmitVO orderSubmitVO = orderService.placeOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * Payment
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation(value = "Pay Order")
    public Result<OrderPaymentVO> payOrder(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        log.info("Pay Order: {}", ordersPaymentDTO);
        OrderPaymentVO paymentVO = orderService.payment(ordersPaymentDTO);
        log.info("Generate transaction slips : {}", paymentVO);

        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        log.info("Pay Success: {}", ordersPaymentDTO.getOrderNumber());
        return Result.success(paymentVO);
    }

    /**
     * Get history orders list
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation(value = "Get History Orders")
    public Result<PageResult> historyOrders(int page,int pageSize, Integer status) {
        PageResult pageResult = orderService.pageQueryHistoryOrders(page,pageSize,status);
        return Result.success(pageResult);
    }

    /**
     * Get order details
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation(value = "Get Order Details By ID")
    public Result<OrderVO> getOrderDetails(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.getOrderDetails(id);
        return Result.success(orderVO);
    }

    /**
     * Cancel order in user side
     * @param id
     * @return
     * @throws Exception
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation(value = "Cancel Order")
    public Result cancelOrder(@PathVariable("id") Long id) {
        orderService.cancelOrderById(id);
        return Result.success();
    }

    /**
     * Order again
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation(value = "Order Again")
    public Result orderAgain(@PathVariable("id") Long id){
        orderService.orderAgain(id);
        return Result.success();
    }

    /**
     * Send reminder
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation(value = "Order Reminder")
    public Result reminder(@PathVariable("id") Long id) {
        orderService.sendReminder(id);
        return Result.success();
    }
}
