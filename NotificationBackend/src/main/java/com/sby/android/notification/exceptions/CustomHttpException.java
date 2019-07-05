package com.sby.android.notification.exceptions;

public class CustomHttpException extends RuntimeException {
    private int statusCode;

    public CustomHttpException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
