package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.entity.user.delete.DeleteUserResponse
import com.teamfilmo.filmo.data.remote.entity.user.info.UserResponse

interface UserDataSource {
    suspend fun getUserInfo(userId: String?): Result<UserResponse>

    suspend fun quitUser(userId: String): Result<DeleteUserResponse>
}
