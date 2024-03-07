package com.teamfilmo.filmo.data.remote.network.policy

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AccessToken

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WriteTimeout

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReadTimeout

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ConnectTimeout

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetryCount

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ContentType
