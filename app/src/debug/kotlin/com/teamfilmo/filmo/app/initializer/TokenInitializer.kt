package com.teamfilmo.filmo.app.initializer

import android.content.Context
import androidx.startup.Initializer
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.data.source.UserTokenSource
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class TokenInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val accessToken = context.getString(R.string.access_token)
        val refreshToken = context.getString(R.string.refresh_token)
        Timber.d("TokenInitializer token: $accessToken, $refreshToken")
        val hilt = EntryPointAccessors.fromApplication(context, InitializerEntry::class.java)

        val userTokenSource = runBlocking { hilt.getUserTokenSource().getUserToken().firstOrNull() }
        if (userTokenSource.isNullOrEmpty()) {
            Timber.d("Token is empty, updating token")
            runBlocking { hilt.getUserTokenSource().updateToken(accessToken, refreshToken) }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface InitializerEntry {
        fun getUserTokenSource(): UserTokenSource
    }
}
