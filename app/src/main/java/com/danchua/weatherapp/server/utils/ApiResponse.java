package com.danchua.weatherapp.server.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by Dan Chua
 */
public class ApiResponse<T> {
    public final int code;

    public final T body;

    public final String errorMessage;

    Gson g = new Gson();

    Error error;

    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = ErrorUtils.getErrorMessage(error);
        error.printStackTrace();
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    String er = response.errorBody().toString();
                    error = g.fromJson(response.errorBody().string(), Error.class);
                    message = error.getMessage();
                    Log.e("ApiError", response.errorBody().toString());
                } catch (IOException e) {
                    Log.e("ApiResult", "error parsing result", e);
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }
}
