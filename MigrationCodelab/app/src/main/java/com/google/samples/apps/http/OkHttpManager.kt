package com.google.samples.apps.http

import android.app.Application
import com.google.samples.apps.http.exception.ApiExceptionHandler
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.Proxy
import java.util.concurrent.TimeUnit

object OkHttpManager {

    private lateinit var config: HttpConfig

    // 使用外部传入的配置对象来初始化配置参数
    fun initialize(config: HttpConfig) {
        this.config = config
    }

    val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(config.timeout, TimeUnit.SECONDS)
            .readTimeout(config.timeout, TimeUnit.SECONDS)
            .writeTimeout(config.timeout, TimeUnit.SECONDS)
            .proxy(Proxy.NO_PROXY)
            .cache(Cache(File(config.cacheDir), config.maxCacheSize))
        config.authenticator?.let { builder.authenticator(it) }
        config.interceptors.forEach { builder.addInterceptor(it) }
        builder.build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(config.baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    class HttpConfig private constructor(
        val app: Application,
        val baseUrl: String,
        val payUrl: String?,
        val timeout: Long,
        val cacheDir: String,
        val maxCacheSize: Long,
        val authenticator: Authenticator?,
        val interceptors: List<Interceptor>,
        val apiExceptionHandler: ApiExceptionHandler?
    ) {

        class Builder(private val app: Application, private val baseUrl: String) {

            private var timeout: Long = HttpConstants.DEFAULT_HTTP_TIMEOUT
            private var cacheDir: String = app.cacheDir.absolutePath.plus(File.separator)
                .plus(HttpConstants.DEFAULT_HTTP_CACHE_DIR)
            private var maxCacheSize: Long = HttpConstants.DEFAULT_HTTP_MAX_CACHE_SIZE
            private var authenticator: Authenticator? = null
            private var interceptors: List<Interceptor> = emptyList()
            private var apiExceptionHandler: ApiExceptionHandler? = null
            private var payUrl: String? = null

            fun setTimeout(timeout: Long): Builder {
                this.timeout = timeout
                return this
            }

            fun setCacheDir(cacheDir: String): Builder {
                this.cacheDir = cacheDir
                return this
            }

            fun setPayUrl(payUrl: String) : Builder {
                this.payUrl = payUrl
                return this
            }

            fun setMaxCacheSize(maxCacheSize: Long): Builder {
                this.maxCacheSize = maxCacheSize
                return this
            }

            fun setAuthenticator(authenticator: Authenticator): Builder {
                this.authenticator = authenticator
                return this
            }

            fun setInterceptors(interceptors: List<Interceptor>): Builder {
                this.interceptors = interceptors
                return this
            }

            fun setApiExceptionHandler(handler: ApiExceptionHandler): Builder {
                this.apiExceptionHandler = handler
                return this
            }

            fun build(): HttpConfig {
                return HttpConfig(
                    app,
                    baseUrl,
                    payUrl,
                    timeout,
                    cacheDir,
                    maxCacheSize,
                    authenticator,
                    interceptors,
                    apiExceptionHandler
                )
            }
        }
    }
}