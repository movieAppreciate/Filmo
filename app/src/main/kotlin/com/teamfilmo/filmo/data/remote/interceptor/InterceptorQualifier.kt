package com.teamfilmo.filmo.data.remote.interceptor

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class LoggingInterceptor

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class HeaderInterceptor

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class AuthInterceptor
