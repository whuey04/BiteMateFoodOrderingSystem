package com.bitemate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String consignee;

    private String phone;

    // 0 = Female, 1 = Male
    private String sex;

    private String provinceCode;

    private String provinceName;

    private String cityCode;

    private String cityName;

    private String districtCode;

    private String districtName;

    // Full detail address
    private String detail;

    private String label;

    // 0 = No, 1 = is default
    private Integer isDefault;

}
