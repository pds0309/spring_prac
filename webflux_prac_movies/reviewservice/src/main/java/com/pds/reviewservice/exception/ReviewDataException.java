package com.pds.reviewservice.exception;

public class ReviewDataException extends RuntimeException {
    private String message;

    public ReviewDataException(String msg) {
        super(msg);
        this.message = msg;
    }

}
