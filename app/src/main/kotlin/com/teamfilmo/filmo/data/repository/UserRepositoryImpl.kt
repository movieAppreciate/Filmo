package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.entity.user.delete.DeleteUserResponse
import com.teamfilmo.filmo.data.remote.entity.user.info.UserResponse
import com.teamfilmo.filmo.data.source.UserDataSource
import com.teamfilmo.filmo.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val userDataSource: UserDataSource,
    ) : UserRepository {
        override suspend fun getUserInfo(userId: String?): Result<UserResponse> = userDataSource.getUserInfo(userId)

        override suspend fun quitUser(userId: String): Result<DeleteUserResponse> = userDataSource.quitUser(userId)
    }
