package com.teamfilmo.filmo.data.remote.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.teamfilmo.filmo.data.remote.network.adapter.ResultCallAdapter
import com.teamfilmo.filmo.data.remote.network.policy.BaseUrl
import com.teamfilmo.filmo.data.remote.network.policy.ConnectTimeout
import com.teamfilmo.filmo.data.remote.network.policy.ContentType
import com.teamfilmo.filmo.data.remote.network.policy.ReadTimeout
import com.teamfilmo.filmo.data.remote.network.policy.RetryCount
import com.teamfilmo.filmo.data.remote.network.policy.ServiceNetworkPolicy
import com.teamfilmo.filmo.data.remote.network.policy.WriteTimeout
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @WriteTimeout
    @Provides
    fun provideWriteTimeout(
        servicePolicy: ServiceNetworkPolicy,
    ): Long = servicePolicy.writeTimeout

    @ReadTimeout
    @Provides
    fun provideReadTimeout(
        servicePolicy: ServiceNetworkPolicy,
    ): Long = servicePolicy.readTimeout

    @ConnectTimeout
    @Provides
    fun provideConnectTimeout(
        servicePolicy: ServiceNetworkPolicy,
    ): Long = servicePolicy.connectTimeout

    @RetryCount
    @Provides
    fun provideRetryCount(
        servicePolicy: ServiceNetworkPolicy,
    ): Int = servicePolicy.retryCount

    @Provides
    @Singleton
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        @RetryCount retryCount: Int,
        okHttpClient: OkHttpClient,
        @ContentType contentType: String,
    ): Retrofit {
        val contentType = contentType.toMediaType()
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(ResultCallAdapter.Factory(retryCount))
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }
}
