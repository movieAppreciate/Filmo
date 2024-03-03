package com.teamfilmo.filmo.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.teamfilmo.filmo.app.policy.ServiceNetworkPolicy
import com.teamfilmo.filmo.data.remote.interceptor.AuthInterceptor
import com.teamfilmo.filmo.data.remote.interceptor.HeaderInterceptor
import com.teamfilmo.filmo.data.remote.interceptor.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @WriteTimeout
    @Provides
    fun provideWriteTimeout(
        serviceNetworkPolicy: ServiceNetworkPolicy,
    ): Long = serviceNetworkPolicy.writeTimeout

    @ReadTimeout
    @Provides
    fun provideReadTimeout(
        serviceNetworkPolicy: ServiceNetworkPolicy,
    ): Long = serviceNetworkPolicy.readTimeout

    @ConnectTimeout
    @Provides
    fun provideConnectTimeout(
        serviceNetworkPolicy: ServiceNetworkPolicy,
    ): Long = serviceNetworkPolicy.connectTimeout

    @RetryCount
    @Provides
    fun provideRetryCount(
        serviceNetworkPolicy: ServiceNetworkPolicy,
    ): Int = serviceNetworkPolicy.retryCount

    @ContentType
    @Provides
    fun provideContentType(): String = "application/json"

    @Provides
    fun provideMediaType(
        @ContentType contentType: String,
    ): MediaType = contentType.toMediaType()

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

    @Provides
    @Singleton
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        @RetryCount retryCount: Int,
        okHttpClient: OkHttpClient,
        mediaType: MediaType,
    ): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(mediaType))
            .build()
    }
}
