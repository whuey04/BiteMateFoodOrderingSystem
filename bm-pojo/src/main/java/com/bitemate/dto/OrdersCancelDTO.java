package com.bitemate.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersCancelDTO implements Serializable {

    private Long id;

    //reason of cancel order
    private String cancelReason;

}
