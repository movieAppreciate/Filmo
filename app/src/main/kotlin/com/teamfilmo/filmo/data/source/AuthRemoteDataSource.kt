package com.teamfilmo.filmo.data.source

interface AuthRemoteDataSource {
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
