package com.google.samples.apps.downloadmodule.reader;

/**
 * Created by hlx-wc on 2015/11/21.
 */
public interface Callback {
    void end();

    void readLoop(int count);
}