package com.google.samples.apps.http.exception;

import com.google.samples.apps.http.exception.NetworkException;

/**
 * Created by hlx-wc on 2017/10/10.
 *
 * exception happen when read data from socket input stream
 */
public class SocketReadException extends NetworkException {

    public SocketReadException(Throwable cause) {
        super(cause);
    }
}
