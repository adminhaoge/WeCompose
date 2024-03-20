package com.google.samples.apps.downloadmodule
import com.google.samples.apps.http.HttpRequest
import com.google.samples.apps.http.exception.ResponseBodyLostException
import com.google.samples.apps.http.exception.UnExpectedResponseCodeException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


abstract class OkHttpContextDataSource<R: HttpRequest, T>: AbstractDataSource<T> {

    constructor(request: R) {
        val call: Call = makeCall(request)
        call.enqueue(mCallback())
    }

    abstract fun makeCall(request: R): Call

    abstract fun parseResult(raw: String): T

    protected fun onFailureImpl(throwable: Throwable?) {
        if (super.setFailure(throwable!!)) {
        }
    }

    protected fun onNewResultImpl(result: T?, isLast: Boolean) {
        if (super.setResult(result, isLast)) {
            if (isLast) {
            }
        }
    }

    inner class mCallback() : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailureImpl(e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (call.isCanceled()) {
                close()
                return
            }

            if (!response.isSuccessful) {
                onFailureImpl(UnExpectedResponseCodeException(response.code))
                return
            }

            response.body?.let { responseBody ->
                try {
                    val rawString = responseBody.string()
                    val result = parseResult(rawString)
                   onNewResultImpl(result, true);
                } catch (e: Exception) {
                    onFailureImpl(e)
                    return
                }


            } ?: run {
                onFailureImpl(ResponseBodyLostException("no response body"))
            }
        }
    }
}

