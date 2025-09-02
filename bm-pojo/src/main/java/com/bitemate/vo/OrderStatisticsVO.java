package com.bitemate.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatisticsVO implements Serializable {
    // Number of orders to be confirmed (awaiting acceptance)
    private Integer toBeConfirmed;

    // Number of orders confirmed (awaiting delivery)
    private Integer confirmed;

    // Number of orders in delivery
    private Integer deliveryInProgress;

}
