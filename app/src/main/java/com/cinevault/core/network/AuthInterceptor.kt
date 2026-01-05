package com.cinevault.core.network

import com.cinevault.core.constant.ApiConstants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader(
            name = "Authorization", value = "Bearer ${ApiConstants.AUTH_KEY}"
        ).addHeader(
            name = "accept", value = "application/json"
        ).build()
        return chain.proceed(request)
    }
}