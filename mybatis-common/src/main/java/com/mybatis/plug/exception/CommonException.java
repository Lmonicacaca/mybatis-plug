package com.mybatis.plug.exception;

/**
 * @author coco
 * @date 2020-08-05 20:07
 **/
public class CommonException extends RuntimeException {

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message) {
        super(message);
    }
}
