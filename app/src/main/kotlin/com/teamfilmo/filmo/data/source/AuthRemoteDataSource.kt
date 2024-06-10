package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.user.LoginResponse
import com.teamfilmo.filmo.data.remote.model.user.SignUpResponse

interface AuthRemoteDataSource {
    suspend fun signUp(
        uid: String,
        type: String,
        profileURL: String?,
    ): Result<SignUpResponse>

    suspend fun login(
        uid: String,
        type: String,
    ): Result<LoginResponse>
}
