package com.google.samples.apps.downloadmodule

import android.util.Pair
import androidx.annotation.Nullable
import java.util.concurrent.Executor

interface DataSource<T> {

    val isClosed: Boolean

    val hasResult: Boolean

    val isFinished: Boolean

    val hasFailed: Boolean

    val getFailureCause: Throwable?

    fun close(): Boolean

    fun <D: DataSubscriber<T>> subscribe(dataSubscriber: D, executor: Executor)

    fun getProgress(): Pair<Long, Long>

    fun getResult(): T?


}