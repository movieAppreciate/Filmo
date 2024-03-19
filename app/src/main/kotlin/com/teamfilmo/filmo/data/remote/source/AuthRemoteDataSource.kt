package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.service.AuthService
import javax.inject.Inject

class AuthRemoteDataSource
    @Inject
    constructor(
        private val authService: AuthService,
    ) {
        suspend fun signUp(
            uid: String,
            type: String,
            profileURL: String?,
        ): Result<String> {
            return authService.signUp(uid, type, profileURL)
        }

        suspend fun login(
            uid: String,
            type: String,
        ): Result<String> {
            return authService.login(uid, type)
        }
    }
