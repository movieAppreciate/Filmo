package com.teamfilmo.filmo.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.teamfilmo.filmo.domain.model.user.UserInfo
import com.teamfilmo.filmo.domain.repository.UserPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : UserPreferencesRepository {
        private val gson = Gson()

        private object PreferencesKeys {
            // 각 플랫폼별 유저 정보 키
            val GOOGLE_USER = stringPreferencesKey("google_user")
            val NAVER_USER = stringPreferencesKey("naver_user")
            val KAKAO_USER = stringPreferencesKey("kakao_user")

            // 마지막 로그인 플랫폼 정보
            val LAST_LOGIN_TYPE = stringPreferencesKey("last_login_type")
        }

        override suspend fun getUserInfoByType(type: String): Flow<UserInfo?> =
            dataStore.data.map { preferences ->
                val key =
                    when (type) {
                        "google" -> PreferencesKeys.GOOGLE_USER
                        "naver" -> PreferencesKeys.NAVER_USER
                        "kakao" -> PreferencesKeys.KAKAO_USER
                        else -> return@map null
                    }
                preferences[key].let {
                    gson.fromJson(it, UserInfo::class.java)
                }
            }

        // 소셜 로그인 유저 정보 저장
        override suspend fun saveUserInfo(userInfo: UserInfo) {
            dataStore.edit { preferences ->
                val type =
                    when (userInfo.type) {
                        "google" -> PreferencesKeys.GOOGLE_USER
                        "naver" -> PreferencesKeys.NAVER_USER
                        "kakao" -> PreferencesKeys.KAKAO_USER
                        else -> throw IllegalArgumentException("Unknown platform type : ${userInfo.type}")
                    }

                preferences[type] = gson.toJson(userInfo)
                preferences[PreferencesKeys.LAST_LOGIN_TYPE] = userInfo.type
            }
        }

        // 특정 타입에 따른 유저 정보 조회 (없으면 해당 타입으로는 회원가입 하지 않은 유저)

        // 유저 정보 조회
        override suspend fun getUserInfo(): Flow<UserInfo?> =
            dataStore.data
                .map { preferences ->
                    val lastLoginType = preferences[PreferencesKeys.LAST_LOGIN_TYPE] ?: return@map null
                    val key =
                        when (lastLoginType) {
                            "google" -> PreferencesKeys.GOOGLE_USER
                            "naver" -> PreferencesKeys.NAVER_USER
                            "kakao" -> PreferencesKeys.KAKAO_USER
                            else -> return@map null
                        }
                    preferences[key].let {
                        gson.fromJson(it, UserInfo::class.java)
                    }
                }

        // 유저 정보 삭제
        override suspend fun clearUserInfo() {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
