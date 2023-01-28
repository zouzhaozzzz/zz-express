package com.zouzhao.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 用来封装给前端的返回数据的
 */
@Data
@NoArgsConstructor
public class Response<T>{
    @ApiModelProperty(
            value = "是否成功",
            required = true
    )
    private boolean success;
    @ApiModelProperty(
            value = "成功/错误的code",
            required = false
    )
    private int code;
    @ApiModelProperty(
            value = "成功/错误的提示信息",
            required = false
    )
    private String msg;
    @ApiModelProperty(
            value = "返回的数据对象",
            required = false
    )
    private T data;

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(boolean success, int code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Response<T> ok(T data) {
        return new Response<T>(true, HttpStatus.OK.value(), "成功", data);
    }

    public static <T> Response<T> ok(String msg) {
        return new Response<T>(true, HttpStatus.OK.value(), msg, null);
    }

    public static <T> Response<T> ok(String msg, T data) {
        return new Response<T>(true, HttpStatus.OK.value(), msg, data);
    }

    public static <T> Response<T> err(T data) {
        return new Response<T>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "失败", data);
    }

    public static <T> Response<T> err(String msg) {
        return new Response<T>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }
    public static <T> Response<T> err(String msg,T data) {
        return new Response<T>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    public static <T> Response<T> err(Integer code,String msg) {
        return new Response<T>(false, code, msg, null);
    }

}
