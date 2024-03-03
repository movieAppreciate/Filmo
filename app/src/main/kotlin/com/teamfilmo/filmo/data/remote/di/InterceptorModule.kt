package com.teamfilmo.filmo.data.remote.di

import com.teamfilmo.filmo.data.di.AccessToken
import com.teamfilmo.filmo.data.di.ContentType
import com.teamfilmo.filmo.data.remote.interceptor.AuthInterceptor
import com.teamfilmo.filmo.data.remote.interceptor.HeaderInterceptor
import com.teamfilmo.filmo.data.remote.interceptor.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {
    @LoggingInterceptor
    @Provides
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @HeaderInterceptor
    @Provides
    fun provideHeaderInterceptor(
        @ContentType contentType: String,
    ): Interceptor =
        Interceptor { chain ->
            val request =
                chain
                    .request()
                    .newBuilder()
                    .addHeader("Content-Type", contentType)
                    .addHeader("User-Agent", System.getProperty("http.agent"))
                    .build()
            chain.proceed(request)
        }

    @AuthInterceptor
    @Provides
    fun provideAuthInterceptor(
        @AccessToken accessToken: String,
    ): Interceptor =
        Interceptor { chain ->
            val request =
                chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
            chain.proceed(request)
        }
}
