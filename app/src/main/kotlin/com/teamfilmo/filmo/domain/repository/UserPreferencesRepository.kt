package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun saveUserInfo(userInfo: UserInfo)

    suspend fun getUserInfo(): Flow<UserInfo?>

    suspend fun clearUserInfo()
}
