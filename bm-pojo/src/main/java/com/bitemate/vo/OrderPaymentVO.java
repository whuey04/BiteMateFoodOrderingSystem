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
public class OrderPaymentVO implements Serializable {

    private String nonceStr;    // Random string (nonce)
    private String paySign;     // Signature
    private String timeStamp;   // Timestamp
    private String signType;    // Signature algorithm
    private String packageStr;  // The 'prepay_id' value returned by the unified order API


}
