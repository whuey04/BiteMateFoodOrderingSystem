package com.bitemate.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersConfirmDTO implements Serializable {

    private Long id;

    // Order status: 1 - Pending Payment, 2 - Awaiting Acceptance, 3 - Accepted, 4 - Delivering, 5 - Completed, 6 - Canceled, 7 - Refunded
    private Integer status;

}
