package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.ui.model.auth.AuthResponse

interface AuthRepository {
    suspend fun signUp(
        uid: String,
        type: String,
        profileURL: String?,
    ): Result<String>

    suspend fun login(
        uid: String,
        type: String,
    ): Result<AuthResponse>
}
