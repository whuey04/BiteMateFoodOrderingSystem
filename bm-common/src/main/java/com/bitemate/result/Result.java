package com.bitemate.result;

import lombok.Data;

import java.io.Serializable;

/**
 * Standard response wrapper for backend API responses.
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; // Code: 1 is success, 0 & others is failed
    private String message; // error message
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.code = 1;
        result.data = object;
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result result = new Result();
        result.message = message;
        result.code = 0;
        return result;
    }
}
