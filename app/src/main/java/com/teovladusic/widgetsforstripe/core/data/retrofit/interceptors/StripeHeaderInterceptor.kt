package com.teovladusic.widgetsforstripe.core.data.retrofit.interceptors

import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class StripeHeaderInterceptor @Inject constructor(private val preferencesManager: PreferencesManager) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        val builder = original.newBuilder()

        builder.addHeader("content-type", "application/json")
        builder.addHeader("accept", "application/json")

        runBlocking {
            val projectId = preferencesManager.selectedStripeProject.first()
            preferencesManager.stripeProjects.first().find { it.id == projectId }?.apiKey
        }?.let {
            builder.addHeader("Authorization", "Bearer $it")
        }

        val request = builder.build()
        return chain.proceed(request)
    }
}
