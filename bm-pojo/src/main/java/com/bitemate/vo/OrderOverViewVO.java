package com.bitemate.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Order Overview VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverViewVO implements Serializable {
    // Number of orders waiting for acceptance
    private Integer waitingOrders;

    // Number of orders waiting for delivery
    private Integer deliveredOrders;

    // Number of completed orders
    private Integer completedOrders;

    // Number of cancelled orders
    private Integer cancelledOrders;

    // Total number of orders
    private Integer allOrders;
}
