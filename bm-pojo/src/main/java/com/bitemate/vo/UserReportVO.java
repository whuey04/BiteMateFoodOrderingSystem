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
public class UserReportVO implements Serializable {

    // List of dates, separated by commas. Example: "2022-10-01,2022-10-02,2022-10-03"
    private String dateList;

    // List of total users per day, separated by commas. Example: "200,210,220"
    private String totalUserList;

    // List of new users per day, separated by commas. Example: "20,21,10"
    private String newUserList;

}
