package com.google.samples.apps.downloadmodule.reader

import com.google.samples.apps.downloadmodule.exception.WriteLocalFileException
import com.google.samples.apps.http.exception.SocketReadException
import java.io.InputStream

interface IReader {
    @Throws(SocketReadException::class, WriteLocalFileException::class)
    fun copy(inputStream: InputStream?, outputStream: IAdapterToStreamAndRaf?, callback: Callback?)
}