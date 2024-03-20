package com.google.samples.apps.downloadmodule.exception;

import java.io.IOException;

/**
 * Created by hlx-wc on 2017/9/7.
 */

public abstract class LocalFileException extends IOException {
    public LocalFileException() {
        super();
    }

    public LocalFileException(Throwable t) {
        super(t);
    }
}
