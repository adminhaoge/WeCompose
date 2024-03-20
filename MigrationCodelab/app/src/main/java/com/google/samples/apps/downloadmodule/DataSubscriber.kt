package com.google.samples.apps.downloadmodule

interface DataSubscriber<T> {

    fun onNewResult(dataSource: DataSource<T>)

    fun onFailure(dataSource: DataSource<T>)

    fun onCancellation(dataSource: DataSource<T>)

    fun onProgressUpdate(dataSource: DataSource<T>)
}