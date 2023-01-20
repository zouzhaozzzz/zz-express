package com.zouzhao.common.config;

import com.zouzhao.common.dto.ResultVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Log4j2
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultVO<String> exceptionHandler(Exception e){
        log.error("统一捕获异常信息：{}",e.getMessage());
        return new ResultVO<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
    }
}
