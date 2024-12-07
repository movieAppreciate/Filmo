package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.user.DeleteUserResponse
import com.teamfilmo.filmo.data.remote.model.user.UserResponse
import com.teamfilmo.filmo.data.remote.service.UserService
import com.teamfilmo.filmo.data.source.UserDataSource
import javax.inject.Inject

class UserDataSourceImpl
    @Inject
    constructor(
        private val userService: UserService,
    ) : UserDataSource {
        override suspend fun getUserInfo(userId: String?): Result<UserResponse> = userService.getUserInfo(userId)

        override suspend fun quitUser(userId: String): Result<DeleteUserResponse> = userService.deleteUser(userId)
    }
