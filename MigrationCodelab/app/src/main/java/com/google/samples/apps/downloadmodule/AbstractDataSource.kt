package com.google.samples.apps.downloadmodule

import android.util.Pair
import androidx.annotation.Nullable
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executor


open class AbstractDataSource<T>: DataSource<T>{

    enum class DataSourceStatus {
        IN_PROGRESS,
        SUCCESS,
        FAILURE,
    }

    private var mFailureThrowable: Throwable? = null
    private var mIsClosed: Boolean = false
    private var mProgress = 0L
    private var mTotal = 0L

    private var mDataSourceStatus: DataSourceStatus = DataSourceStatus.IN_PROGRESS

    private val mSubscribers: ConcurrentLinkedQueue<Pair<DataSubscriber<T>, Executor>> =
        ConcurrentLinkedQueue()

    private var mResult: T? = null

    override val isClosed: Boolean
        get() = mIsClosed
    override val hasResult: Boolean
        get() = mResult != null
    override val isFinished: Boolean
        get() = mDataSourceStatus != DataSourceStatus.IN_PROGRESS
    override val hasFailed: Boolean
        get() = mDataSourceStatus == DataSourceStatus.FAILURE
    override val getFailureCause: Throwable?
        get() = mFailureThrowable

    override fun close(): Boolean {
        var resultToClose: T?
        synchronized(this) {
            if (mIsClosed) {
                return false
            }
            mIsClosed = true
            mResult?.let {
                resultToClose = it
                mResult = null
            }
        }

        if (!isFinished) {
            notifyDataSubscribers()
        }
        synchronized (this) {
            mSubscribers.clear()
        }
        return true
    }
    override fun getProgress(): Pair<Long, Long> {
        return Pair(mProgress, mTotal)
    }

    override fun getResult(): T? {
        return mResult
    }

    override fun <D : DataSubscriber<T>> subscribe(dataSubscriber: D, executor: Executor) {
        val shouldNotify: Boolean
        synchronized(this) {
            if (mIsClosed) {
                return
            }
            if (mDataSourceStatus == DataSourceStatus.IN_PROGRESS) {
                mSubscribers.plus(Pair.create(dataSubscriber, executor))
            }
            shouldNotify = hasResult || isFinished || wasCancelled()
        }

        if (shouldNotify) {
            notifyDataSubscriber(dataSubscriber, executor, hasFailed, wasCancelled())
        }
    }

    protected fun setProgress(progress: Long, total: Long): Boolean {
        val result = setProgressInternal(progress, total)
        return result.apply {
            if (this) notifyProgressUpdate()
        }
    }

    protected fun setResult(value: T?, isLast: Boolean): Boolean {
       val result = setResultInternal(value, isLast)
        if (result) {
            notifyDataSubscribers();
        }
        return result
    }

    protected fun setFailure(throwable: Throwable): Boolean {
        val result = setFailureInternal(throwable)
        if (result) {
            notifyDataSubscribers()
        }
        return result
    }

    @Synchronized
    private fun setProgressInternal(progress: Long, total: Long): Boolean {
        return when {
            (mIsClosed || mDataSourceStatus != DataSourceStatus.IN_PROGRESS) -> false
            (progress < mProgress) -> false
            else -> {
                mTotal = total
                mProgress = progress
                true
            }
        }
    }

    private fun setResultInternal(value: T?, last: Boolean): Boolean {
        var resultToClose: T? = null
        try {
            synchronized(this) {
                return if (mIsClosed || mDataSourceStatus != DataSourceStatus.IN_PROGRESS) {
                    resultToClose = value
                    false
                } else {
                    if (last) {
                        mDataSourceStatus = DataSourceStatus.SUCCESS
                    }

                    if (mResult != null) {
                        resultToClose = mResult
                        mResult = value
                    }
                    true
                }
            }
        } finally {
            resultToClose?.let { closeResult(it) }
        }
    }

    @Synchronized
    private fun setFailureInternal(throwable: Throwable): Boolean {
        return if (mIsClosed || mDataSourceStatus != DataSourceStatus.IN_PROGRESS) {
            false
        } else {
            mDataSourceStatus = DataSourceStatus.FAILURE
            mFailureThrowable = throwable
            true
        }
    }

    private fun notifyDataSubscribers() {
        mSubscribers.map { pair ->
            notifyDataSubscriber(pair.first, pair.second, hasFailed, wasCancelled())
        }
    }

    private fun notifyDataSubscriber(
        dataSubscriber: DataSubscriber<T>,
        executor: Executor,
        isFailure: Boolean,
        isCancellation: Boolean
    ) {
        executor.execute {
            when {
                isFailure -> dataSubscriber.onFailure(this)
                isCancellation -> dataSubscriber.onCancellation(this)
                else ->  dataSubscriber.onNewResult(this)
            }
        }
    }

    protected fun notifyProgressUpdate() {
        mSubscribers.map { pair ->
            val subscriber = pair.first
            val executor = pair.second
            executor.execute {
                subscriber.onProgressUpdate(this)
            }
        }
    }

    private fun wasCancelled(): Boolean {
        return isClosed && !isFinished
    }

    protected fun closeResult(result: T) {
        // default implementation does nothing
    }

}