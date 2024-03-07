package com.teamfilmo.filmo.data.remote.network.policy

data class ServiceNetworkPolicy(
    val writeTimeout: Long,
    val readTimeout: Long,
    val connectTimeout: Long,
    val retryCount: Int,
)
