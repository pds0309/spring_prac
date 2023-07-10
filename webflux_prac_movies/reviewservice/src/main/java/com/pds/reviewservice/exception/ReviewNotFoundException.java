package com.pds.reviewservice.exception;

public class ReviewNotFoundException extends RuntimeException {
    private String message;

    public ReviewNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
