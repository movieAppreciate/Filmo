package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.entity.user.delete.DeleteUserResponse
import com.teamfilmo.filmo.data.remote.entity.user.info.UserResponse

interface UserRepository {
    suspend fun getUserInfo(userId: String?): Result<UserResponse>

    suspend fun quitUser(userId: String): Result<DeleteUserResponse>
}
