package com.teamfilmo.filmo.data.remote.network.interceptor.di

import com.teamfilmo.filmo.data.local.source.UserTokenSource
import com.teamfilmo.filmo.data.remote.network.interceptor.AuthInterceptor
import com.teamfilmo.filmo.data.remote.network.interceptor.HeaderInterceptor
import com.teamfilmo.filmo.data.remote.network.interceptor.LoggingInterceptor
import com.teamfilmo.filmo.data.remote.network.interceptor.UserAuthInterceptor
import com.teamfilmo.filmo.data.remote.network.policy.ContentType
import com.teamfilmo.filmo.data.remote.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
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
        userTokenSource: Provider<UserTokenSource>,
        authService: Provider<AuthService>,
    ): Interceptor =
        UserAuthInterceptor(
            userTokenSource,
            authService,
        )
}
