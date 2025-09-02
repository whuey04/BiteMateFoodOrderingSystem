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
public class SalesTop10ReportVO implements Serializable {

    //List of product names, separated by commas.
    private String nameList;

    // List of sales volumes, separated by commas. Example: "260,215,200"
    private String numberList;

}
