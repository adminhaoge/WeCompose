package com.google.samples.apps.downloadmodule.reader;

import java.io.IOException;

/**
 * Created by hlx-wc on 2015/11/21.
 * <p/>
 * Adapter interface to make OutputStream and RandomAccessFile implement the same interface.
 */
public interface IAdapterToStreamAndRaf {
    void write(byte[] buffer) throws IOException;

    void write(byte[] buffer, int offset, int count) throws IOException;

    void write(int oneByte) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;
}
