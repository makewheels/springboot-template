package com.github.makewheels.springboottemplate.springboot.exception;


import com.github.makewheels.springboottemplate.system.response.ErrorCode;

public class VideoException extends RuntimeException {
    private final ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public VideoException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public VideoException(String message) {
        super(message);
        this.errorCode = ErrorCode.FAIL;
    }

    public VideoException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
