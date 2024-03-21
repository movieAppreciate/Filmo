package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.source.AuthRemoteDataSource
import javax.inject.Inject

class AuthRepository
    @Inject
    constructor(
        private val authRemoteDataSource: AuthRemoteDataSource,
    ) {
        suspend fun signUp(
            uid: String,
            type: String,
            profileURL: String?,
        ): Result<String> {
            return authRemoteDataSource.signUp(uid, type, profileURL)
        }

        suspend fun login(
            uid: String,
            type: String,
        ): Result<String> {
            return authRemoteDataSource.login(uid, type)
        }
    }
