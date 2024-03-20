package com.google.samples.apps.http.exception;

/**
 * Created by hlx-wc on 2017/10/10.
 *
 * unexpected http response code, not 200 or 206
 */
public class UnExpectedResponseCodeException extends NetworkException {

    private final int mCode;

    public UnExpectedResponseCodeException(int code) {
        super("unexpected code " + code);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }
}
