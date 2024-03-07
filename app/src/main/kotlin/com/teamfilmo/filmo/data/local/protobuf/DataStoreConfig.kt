package com.teamfilmo.filmo.data.local.protobuf

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.teamfilmo.filmo.data.local.model.UserToken
import com.teamfilmo.filmo.data.local.serializer.UserTokenSerializer

object DataStoreConfig {
    const val USER_TOKEN_DATA_STORE_FILE_NAME = "user_token.pb"
}

val Context.userToken: DataStore<UserToken> by dataStore(
    fileName = DataStoreConfig.USER_TOKEN_DATA_STORE_FILE_NAME,
    serializer = UserTokenSerializer,
)
