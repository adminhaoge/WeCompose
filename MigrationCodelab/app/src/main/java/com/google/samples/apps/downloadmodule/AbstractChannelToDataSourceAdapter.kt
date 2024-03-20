package com.google.samples.apps.downloadmodule

import com.google.samples.apps.downloadmodule.channel.Channel
import com.google.samples.apps.downloadmodule.channel.context.ChannelContext
import com.google.samples.apps.downloadmodule.channel.monitor.Monitor

abstract class AbstractChannelToDataSourceAdapter<T>(
    protected val channel: Channel<T, ChannelContext>
): AbstractDataSource<T> {
    init {
        channel.open(createMonitor())
    }

    private fun createMonitor(): Monitor<T>? = BaseMo
}