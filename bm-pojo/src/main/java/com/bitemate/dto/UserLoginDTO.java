package com.bitemate.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * User Login in client side
 */
@Data
public class UserLoginDTO implements Serializable {

    private String email;
    private String code;

}
