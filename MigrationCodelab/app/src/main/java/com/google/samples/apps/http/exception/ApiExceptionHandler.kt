package com.google.samples.apps.http.exception

/**
 * 处理Api接口特殊异常响应
 */
abstract class ApiExceptionHandler {

    /**
     * @param errorCode 异常响应码
     * @return 该异常是否已经被 ApiExceptionHandler 处理过。true(已处理)、false(未处理)
     */
    abstract fun handleException(errorCode: Int, errorMsg: String): Boolean
}