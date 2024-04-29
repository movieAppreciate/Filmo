package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.source.AuthRemoteDataSource
import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authRemoteDataSource: AuthRemoteDataSource,
    ) : AuthRepository {
        override suspend fun signUp(
            uid: String,
            type: String,
            profileURL: String?,
        ): Result<String> {
            return authRemoteDataSource.signUp(uid, type, profileURL)
        }

        override suspend fun login(
            uid: String,
            type: String,
        ): Result<String> {
            return authRemoteDataSource.login(uid, type)
        }
    }
