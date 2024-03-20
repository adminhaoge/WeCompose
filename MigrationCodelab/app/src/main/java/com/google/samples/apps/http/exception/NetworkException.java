package com.google.samples.apps.http.exception;

/**
 * Created by hlx-wc on 2017/9/7.
 */

/**
 * network exception
 */
public abstract class NetworkException extends Exception {
    NetworkException(String message) {
        super(message);
    }

    NetworkException(Throwable throwable) {
        super(throwable);
    }
}
