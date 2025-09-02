package com.bitemate.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Business Data Overview
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDataVO implements Serializable {

    private Double turnover;// Total revenue

    private Integer validOrderCount;// Number of valid orders

    private Double orderCompletionRate;// Order completion rate

    private Double unitPrice;// Average order value (AOV)

    private Integer newUsers;// Number of new users

}
