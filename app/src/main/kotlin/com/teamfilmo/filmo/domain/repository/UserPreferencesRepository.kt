package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.domain.model.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun getUserInfoByType(type: String): Flow<UserInfo?>

    suspend fun saveUserInfo(userInfo: UserInfo)

    suspend fun getUserInfo(): Flow<UserInfo?>

    suspend fun clearUserInfo()
}
