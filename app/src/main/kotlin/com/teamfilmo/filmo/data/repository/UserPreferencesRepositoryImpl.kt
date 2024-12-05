package com.teamfilmo.filmo.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.teamfilmo.filmo.data.remote.model.user.UserInfo
import com.teamfilmo.filmo.domain.repository.UserPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : UserPreferencesRepository {
        private object PreferencesKeys {
            val USER_ID = stringPreferencesKey("userId")
            val NICK_NAME = stringPreferencesKey("nick_name")
            val ROLES = stringPreferencesKey("roles")
        }

        // 유저 정보 저장
        override suspend fun saveUserInfo(userInfo: UserInfo) {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.USER_ID] = userInfo.userId
                preferences[PreferencesKeys.NICK_NAME] = userInfo.nickName
                preferences[PreferencesKeys.ROLES] = userInfo.roles
            }
        }

        // 유저 정보 조회
        override suspend fun getUserInfo(): Flow<UserInfo?> =
            dataStore.data
                .map { preferences ->
                    val userId = preferences[PreferencesKeys.USER_ID]
                    if (userId.isNullOrEmpty()) {
                        null
                    } else {
                        UserInfo(
                            userId = userId,
                            nickName = preferences[PreferencesKeys.NICK_NAME] ?: "",
                            roles = preferences[PreferencesKeys.ROLES] ?: "",
                        )
                    }
                }

        // 유저 정보 삭제
        override suspend fun clearUserInfo() {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
