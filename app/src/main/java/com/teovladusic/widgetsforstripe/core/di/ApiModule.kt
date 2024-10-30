package com.teovladusic.widgetsforstripe.core.di

import com.teovladusic.widgetsforstripe.core.data.api.StripeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideStripeApi(retrofit: Retrofit): StripeApi =
        retrofit.create(StripeApi::class.java)
}
