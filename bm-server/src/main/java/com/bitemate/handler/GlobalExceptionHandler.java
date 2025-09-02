package com.bitemate.handler;

import com.bitemate.constant.MessageConstant;
import com.bitemate.exception.BaseException;
import com.bitemate.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Global exception handler to manage business exceptions thrown in the application
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles business exceptions
     * @param e
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException e){
        log.error("Error message:{}",e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException e){
        //Duplicate entry username for key 'employee.idx_username'
        String message = e.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = "Username: " + username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
