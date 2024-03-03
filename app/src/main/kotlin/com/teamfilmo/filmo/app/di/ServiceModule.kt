package com.teamfilmo.filmo.app.di

import android.content.Context
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.app.policy.ServiceNetworkPolicy
import com.teamfilmo.filmo.data.di.AccessToken
import com.teamfilmo.filmo.data.di.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @BaseUrl
    fun provideBaseUrl(
        @ApplicationContext context: Context,
    ): String = context.getString(R.string.service_url)

    @Provides
    @AccessToken
    fun provideAccessToken(
        @ApplicationContext context: Context,
    ): String = context.getString(R.string.access_token)

    @Provides
    fun provideNetworkPolicy(
        @ApplicationContext context: Context,
    ): ServiceNetworkPolicy =
        ServiceNetworkPolicy(
            writeTimeout = context.resources.getInteger(R.integer.write_timeout).toLong(),
            readTimeout = context.resources.getInteger(R.integer.read_timeout).toLong(),
            connectTimeout = context.resources.getInteger(R.integer.connect_timeout).toLong(),
            retryCount = context.resources.getInteger(R.integer.max_retry_count),
        )
}
