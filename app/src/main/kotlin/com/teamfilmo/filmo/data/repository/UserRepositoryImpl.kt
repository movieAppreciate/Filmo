package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.user.UserQuitRequest
import com.teamfilmo.filmo.data.remote.model.user.UserQuitResponse
import com.teamfilmo.filmo.data.remote.model.user.UserResponse
import com.teamfilmo.filmo.data.source.UserDataSource
import com.teamfilmo.filmo.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val userDataSource: UserDataSource,
    ) : UserRepository {
        override suspend fun getUserInfo(userId: String?): Result<UserResponse> = userDataSource.getUserInfo(userId)

        override suspend fun quitUser(userId: UserQuitRequest): Result<UserQuitResponse> = userDataSource.quitUser(userId)
    }
