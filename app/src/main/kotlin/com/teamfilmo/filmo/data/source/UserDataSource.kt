package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.user.UserQuitRequest
import com.teamfilmo.filmo.data.remote.model.user.UserQuitResponse
import com.teamfilmo.filmo.data.remote.model.user.UserResponse

interface UserDataSource {
    suspend fun getUserInfo(userId: String?): Result<UserResponse>

    suspend fun quitUser(userId: UserQuitRequest): Result<UserQuitResponse>
}
