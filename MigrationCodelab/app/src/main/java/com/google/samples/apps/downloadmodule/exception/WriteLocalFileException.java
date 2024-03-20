package com.google.samples.apps.downloadmodule.exception;


/**
 * Created by hlx-wc on 2017/10/10.
 *
 * exception happen when write stream data to local file
 */
public class WriteLocalFileException extends LocalFileException {
    public WriteLocalFileException(Throwable t) {
        super(t);
    }
}
