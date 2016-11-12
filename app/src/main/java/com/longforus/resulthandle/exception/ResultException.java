package com.longforus.resulthandle.exception;

/**
 * Created by Void Young on 11/12/2016  8:21 PM.
 * Description : 自定义要抛出的异常
 */

public class ResultException extends RuntimeException {
    private int errorCode;

    public ResultException(String message) {
        super(message);
    }

    public ResultException() {
        super();
    }
    public ResultException(String message,int code) {
        this(message);
        errorCode = code;
    }

    @Override
    public String toString() {
        return "ErrorCode = "+errorCode +  super.toString();
    }

    public ResultException(Throwable cause) {
        super(cause);
    }
}
