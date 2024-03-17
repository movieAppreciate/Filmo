package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.local.source.impl.UserTokenSourceImpl
import com.teamfilmo.filmo.data.remote.service.AuthService
import javax.inject.Inject

class AuthRepository
    @Inject
    constructor(
        private val authService: AuthService,
        private val userTokenSourceImpl: UserTokenSourceImpl,
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
