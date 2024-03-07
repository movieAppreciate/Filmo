package com.teamfilmo.filmo.data.local.source.impl

import androidx.datastore.core.DataStore
import com.teamfilmo.filmo.data.local.model.UserToken
import com.teamfilmo.filmo.data.local.model.copy
import com.teamfilmo.filmo.data.local.model.userToken
import com.teamfilmo.filmo.data.local.source.UserTokenSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserTokenSourceImpl
    @Inject
    constructor(
        private val dataStore: DataStore<UserToken>,
    ) : UserTokenSource {
        override fun getUserToken(): Flow<String> {
            return dataStore.data.map {
                it.accessToken
            }
        }

        override suspend fun setUserToken(token: String) {
            dataStore.updateData {
                it.copy {
                    accessToken = token
                }
            }
        }

        override fun getRefreshToken(): Flow<String> {
            return dataStore.data.map {
                it.refreshToken
            }
        }

        override suspend fun setRefreshToken(token: String) {
            dataStore.updateData {
                it.copy {
                    refreshToken = token
                }
            }
        }

        override suspend fun updateToken(
            accessToken: String,
            refreshToken: String,
        ) {
            dataStore.updateData {
                it.copy {
                    this.accessToken = accessToken
                    this.refreshToken = refreshToken
                }
            }
        }

        override suspend fun clearUserToken() {
            dataStore.updateData {
                userToken {
                    clearAccessToken()
                    clearRefreshToken()
                }
            }
        }
    }
