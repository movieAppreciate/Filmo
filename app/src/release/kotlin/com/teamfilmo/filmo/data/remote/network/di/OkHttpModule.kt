package com.teamfilmo.filmo.data.remote.network.di

import com.teamfilmo.filmo.data.remote.network.interceptor.AuthInterceptor
import com.teamfilmo.filmo.data.remote.network.interceptor.HeaderInterceptor
import com.teamfilmo.filmo.data.remote.network.interceptor.LoggingInterceptor
import com.teamfilmo.filmo.data.remote.network.policy.ConnectTimeout
import com.teamfilmo.filmo.data.remote.network.policy.ReadTimeout
import com.teamfilmo.filmo.data.remote.network.policy.WriteTimeout
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @WriteTimeout writeTimeout: Long,
        @ReadTimeout readTimeout: Long,
        @ConnectTimeout connectTimeout: Long,
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @HeaderInterceptor headerInterceptor: Interceptor,
        @AuthInterceptor authInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(authInterceptor)
            .build()
}
