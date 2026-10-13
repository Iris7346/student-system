package com.example.studentsystem.common;

import lombok.Data;

/**
 * 统一返回结果类
 */
@Data //Lombok提供，自动生成getter、setter和toString
public class Result<T> {
    private Integer code; //状态码(200表示成功/500表示失败...)
    private String message; //提示信息
    private T data; //返回的具体数据(User/List...)

    //成功时的返回方法(带数据)
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    //成功时的返回方法(不带数据，比如删除成功)
    public static <T> Result<T> success() {
        return success(null);
    }

    //失败时的返回方法
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    //自定义状态码的失败方法
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}