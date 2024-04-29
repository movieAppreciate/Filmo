package com.teamfilmo.filmo.domain.repository

interface AuthRepository {
    suspend fun signUp(
        uid: String,
        type: String,
        profileURL: String?,
    ): Result<String>

    suspend fun login(
        uid: String,
        type: String,
    ): Result<String>
}
