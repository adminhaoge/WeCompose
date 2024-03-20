package com.google.samples.apps.downloadmodule.reader

import com.google.samples.apps.downloadmodule.utils.ByteArrayPool
import java.io.Closeable

abstract class DownloadReader(byteArrayPool: ByteArrayPool?): IReader, Closeable {
    var mByteArrayPool: ByteArrayPool? = byteArrayPool
}