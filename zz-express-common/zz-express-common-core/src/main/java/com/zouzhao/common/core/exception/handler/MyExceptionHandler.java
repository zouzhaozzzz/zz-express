package com.zouzhao.common.core.exception.handler;

import com.zouzhao.common.core.exception.MyException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Log4j2
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {MyException.class})
    public ResponseEntity<?> exceptionHandler(Exception e){
        log.error("统一捕获异常信息：{}",e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
