package com.bitemate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Response model for employee login")
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("Primary key ID")
    private Long id;

    @ApiModelProperty("Username")
    private String userName;

    @ApiModelProperty("Name")
    private String name;

    @ApiModelProperty("JWT Token")
    private String token;

}
