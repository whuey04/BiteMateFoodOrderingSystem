package com.bitemate.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportVO implements Serializable {

    // Dates, separated by commas. Example: "2022-10-01,2022-10-02,2022-10-03"
    private String dateList;

    // Daily order counts, separated by commas. Example: "260,210,215"
    private String orderCountList;

    // Daily valid order counts, separated by commas. Example: "20,21,10"
    private String validOrderCountList;

    // Total number of orders
    private Integer totalOrderCount;

    // Total number of valid orders
    private Integer validOrderCount;

    // Order completion rate
    private Double orderCompletionRate;

}
