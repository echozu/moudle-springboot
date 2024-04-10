package com.echo.handler;

import com.echo.constant.MessageConstant;
import com.echo.constant.MessageConstant;
import com.echo.exception.BaseException;
import com.echo.result.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 * 捕捉并抛出异常便于前后端【自定义异常】
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常：这里的作用就是去捕获异常的信息并抛出
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //这里是处理主键异常的，即在新增操作时，如果主键冲突【如用户名唯一但又插入了一条同名用户，则会报错】
        String message = ex.getMessage();
        //根据报错信息去截取：这里可以得到更为准确的用户id
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
