package com.bitemate.vo;

import com.bitemate.entity.OrderDetail;
import com.bitemate.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    //Ordered dish summary
    private String orderDishes;

    // Detailed information for each item in the order
    private List<OrderDetail> orderDetailList;

}
