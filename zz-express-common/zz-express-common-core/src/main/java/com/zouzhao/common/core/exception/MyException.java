package com.zouzhao.common.core.exception;

/**
 * @author 姚超
 * @DATE: 2023-2-26
 */
public class MyException extends RuntimeException{
    public MyException() {
        super();
    }

    public MyException(String message) {
        super(message);
    }
}
