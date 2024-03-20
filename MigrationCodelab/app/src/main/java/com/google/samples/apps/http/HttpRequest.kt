package com.google.samples.apps.http

import android.net.Uri
import com.google.samples.apps.http.utils.Preconditions
import okhttp3.Headers
import java.lang.reflect.Parameter
import java.util.Collections


class HttpRequest(
    private var mHost: String? = null,
    private var mPath: String? = null,
    private var mMethod: Method? = null,
    private var mQuery: List<Query>? = null,
    private var mParameter: List<Parameter>? = null,
    private var mHeaders: Headers? = null,
) {

    fun getHost(): String? {
        return mHost
    }

    fun getPath(): String? {
        return mPath
    }

    fun getMethod(): Method? {
        return mMethod
    }

    fun getQuery(): List<Query> {
        return Collections.unmodifiableList(mQuery)
    }

    fun getParameter(): List<Parameter> {
        return Collections.unmodifiableList(mParameter)
    }

    fun getHeaders(): Headers? {
        return mHeaders
    }

    fun url(): String {
        val builder = StringBuilder(mHost)
        if (FP.notEmpty(mPath)) builder.append(mPath)
        val parse = Uri.parse(builder.toString())
        val uriBuilder = Uri.Builder()
        uriBuilder.scheme(parse.scheme)
            .encodedAuthority(parse.authority)
            .path(parse.path)
            .encodedQuery(parse.encodedQuery)
            .fragment(parse.fragment)
        if (FP.notEmpty(mQuery)) {
            for (i in mQuery!!.indices) {
                val query: Query = mQuery!![i]
                uriBuilder.appendQueryParameter(query.getKey(), query.getValue())
            }
        }
        return uriBuilder.build().toString()
    }

    override fun toString(): String {
        return "HttpRequest{" +
                "mHost='" + mHost + '\'' +
                ", mPath='" + mPath + '\'' +
                ", mMethod=" + mMethod +
                ", mQuery=" + mQuery +
                ", mParameter=" + mParameter +
                '}'
    }


    class Builder internal constructor() {
        private var mHost: String? = null
        private var mPath: String? = null
        private var mMethod: Method
        private val mQuery: MutableList<Query> = ArrayList<Query>()
        private val mParameter: MutableList<Parameter> = ArrayList()
        private val mBuilder: Headers.Builder = Builder()

        /**
         * default method is get
         */
        init {
            mMethod = Method.GET
        }

        fun host(host: String?): Builder {
            mHost = host
            return this
        }

        fun path(path: String?): Builder {
            mPath = path
            return this
        }

        fun get(): Builder {
            mMethod = Method.GET
            return this
        }

        fun post(): Builder {
            mMethod = Method.POST
            return this
        }

        fun query(query: Query): Builder {
            mQuery.add(Preconditions.checkNotNull(query))
            return this
        }

        fun query(key: String?, value: String?): Builder {
            mQuery.add(Query(key, value))
            return this
        }

        fun parameter(parameter: Parameter): Builder {
            mMethod = Method.POST
            mParameter.add(Preconditions.checkNotNull(parameter))
            return this
        }

        fun parameter(key: String?, value: String?): Builder {
            mMethod = Method.POST
            mParameter.add(Parameter(key, value))
            return this
        }

        fun parameter(parameters: List<Parameter>): Builder {
            if (FP.empty(parameters)) return this
            mMethod = Method.POST
            for (parameter in parameters) {
                mParameter.add(parameter)
            }
            return this
        }

        fun header(key: String, value: String): Builder {
            mBuilder.add(
                Preconditions.checkNotNull(key),
                Preconditions.checkNotNull(value)
            )
            return this
        }

        fun build(): HttpRequest {
            return HttpRequest(mMethod, mHost, mPath, mQuery, mParameter, mBuilder.build())
        }

        companion object {
            fun newBuilder(): Builder {
                return Builder()
            }
        }
    }

}