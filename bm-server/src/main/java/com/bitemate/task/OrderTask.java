package com.bitemate.task;

import com.bitemate.entity.Orders;
import com.bitemate.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * Process Timeout Order
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        log.info("Process timeout order: {}", new Date());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList = orderMapper.getOrderByStatusAndOrdertime(Orders.PENDING_PAYMENT, time);
        if(ordersList == null && ordersList.size() > 0) {
            ordersList.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("Payment Timeout, Cancel Automatically");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.updateOrder(order);
            });
        }
    }

    /**
     * Process Delivery order
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("Process Delivery Order：{}", new Date());LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getOrderByStatusAndOrdertime(Orders.DELIVERY_IN_PROGRESS, time);

        if(ordersList != null && ordersList.size() > 0){
            ordersList.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                orderMapper.updateOrder(order);
            });
        }
    }
}
