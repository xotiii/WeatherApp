package com.danchua.weatherapp.server.utils;

/**
 * Created by Dan Chua
 */
public class Error {

    private boolean error;

    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}