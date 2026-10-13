package com.example.studentsystem.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 作用：任何一个 Controller 抛出异常，都会被这里捕获，并返回统一的 Result 格式给前端
 */
@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(RuntimeException.class) //捕获所有RuntimeException
    public Result<String> handleRuntimeException(RuntimeException e){
        //返回 500 错误码和异常里的提示信息
        return Result.error(e.getMessage());
    }
}