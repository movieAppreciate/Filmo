package com.teamfilmo.filmo.app.policy

data class ServiceNetworkPolicy(
    val writeTimeout: Long,
    val readTimeout: Long,
    val connectTimeout: Long,
    val retryCount: Int,
)
