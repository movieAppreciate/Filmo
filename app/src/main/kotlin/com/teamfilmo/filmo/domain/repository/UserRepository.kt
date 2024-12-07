package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.user.DeleteUserResponse
import com.teamfilmo.filmo.data.remote.model.user.UserResponse

interface UserRepository {
    suspend fun getUserInfo(userId: String?): Result<UserResponse>

    suspend fun quitUser(userId: String): Result<DeleteUserResponse>
}
