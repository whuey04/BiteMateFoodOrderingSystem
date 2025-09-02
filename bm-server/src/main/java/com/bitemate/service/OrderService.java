package com.bitemate.service;

import com.bitemate.dto.*;
import com.bitemate.result.PageResult;
import com.bitemate.vo.OrderPaymentVO;
import com.bitemate.vo.OrderStatisticsVO;
import com.bitemate.vo.OrderSubmitVO;
import com.bitemate.vo.OrderVO;

public interface OrderService {
    /**
     * Place order
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO placeOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * Order payment
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);

    /**
     * Purchase completed, update order status
     * @param orderNumber
     */
    void paySuccess(String orderNumber);

    /**
     * Get history orders list
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQueryHistoryOrders(int page, int pageSize, Integer status);

    /**
     * Get order details
     * @param id
     * @return
     */
    OrderVO getOrderDetails(Long id);

    /**
     * Cancel order
     * @param id
     */
    void cancelOrderById(Long id);

    /**
     * Order again
     * @param id
     */
    void orderAgain(Long id);

    /**
     * Condition Search
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult orderConditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO getOrderStatistics();

    /**
     * Confirm order
     * @param ordersConfirmDTO
     */
    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * Reject order
     * @param ordersRejectionDTO
     */
    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * Cancel order by admin
     * @param ordersCancelDTO
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * Delivery order
     * @param id
     */
    void deliveryOrder(Long id);

    /**
     * Complete Order
     * @param id
     */
    void completeOrder(Long id);

    /**
     * Send reminder
     * @param id
     */
    void sendReminder(Long id);
}
