package com.teovladusic.widgetsforstripe.core.di

import com.google.gson.GsonBuilder
import com.teovladusic.widgetsforstripe.BuildConfig
import com.teovladusic.widgetsforstripe.core.data.api_result.ApiResultAdapterFactory
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import com.teovladusic.widgetsforstripe.core.data.retrofit.interceptors.StripeHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private const val TIMEOUT_SECONDS = 60L

    @Provides
    @Singleton
    fun provideStripeHeaderInterceptor(preferencesManager: PreferencesManager): StripeHeaderInterceptor =
        StripeHeaderInterceptor(preferencesManager)

    @Provides
    @Singleton
    fun provideOkHttpClient(stripeHeaderInterceptor: StripeHeaderInterceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)

        val clientBuilder = OkHttpClient.Builder()

        clientBuilder.addInterceptor(stripeHeaderInterceptor)

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        clientBuilder.readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        createRetrofit(BuildConfig.BASE_URL, okHttpClient)

    private fun createRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .addCallAdapterFactory(ApiResultAdapterFactory())
            .build()
}
