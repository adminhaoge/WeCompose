package com.google.samples.apps.downloadmodule.channel.context

import com.google.samples.apps.downloadmodule.order.Order
import com.google.samples.apps.downloadmodule.reader.DownloadReader
import java.io.Closeable


interface ChannelContext: Closeable {
    val order: Order
    val progressInterval: Long
    val progressTimeInterval: Long
    val checkDiskSpaceTimeInterval: Long
    fun getDownloadReader(progress: Long): DownloadReader?
}