package com.google.samples.apps.downloadmodule.channel

import com.google.samples.apps.downloadmodule.channel.context.ChannelContext
import com.google.samples.apps.downloadmodule.channel.monitor.Monitor


interface Channel<T, P: ChannelContext> {
    fun open(monitor: Monitor<T>?, context: P)
}