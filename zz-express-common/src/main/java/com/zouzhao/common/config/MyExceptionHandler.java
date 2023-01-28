package com.zouzhao.common.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Log4j2
public class MyExceptionHandler {

    // @ResponseBody
    // @ExceptionHandler(value = {Exception.class,RuntimeException.class})
    // public Response<String> exceptionHandler(Exception e){
    //     log.error("统一捕获异常信息：{}",e.getMessage());
    //     return Response.err("失败",e.getMessage());
    // }
}
