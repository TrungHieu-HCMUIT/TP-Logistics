package com.trunnghieu.tplogisticsapplication.data.api.helper

import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    companion object {
        const val KEY_APP_PROTOCOL = "X-App-Protocol"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestBuilder = request.newBuilder()
        requestBuilder.header(KEY_APP_PROTOCOL, ApiConst.APP_PROTOCOL)
        request = requestBuilder.build()
        return chain.proceed(request)
    }

}