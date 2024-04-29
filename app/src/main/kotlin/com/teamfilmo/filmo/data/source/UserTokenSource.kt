package com.teamfilmo.filmo.data.source

import kotlinx.coroutines.flow.Flow

interface UserTokenSource {
    fun getUserToken(): Flow<String>

    suspend fun setUserToken(token: String)

    fun getRefreshToken(): Flow<String>

    suspend fun setRefreshToken(token: String)

    suspend fun updateToken(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun clearUserToken()
}
