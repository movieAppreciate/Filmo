package com.teamfilmo.filmo.data.local.protobuf.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.teamfilmo.filmo.data.local.model.UserToken
import com.teamfilmo.filmo.data.local.protobuf.userToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    fun provideDataStoreUserToken(
        @ApplicationContext context: Context,
    ): DataStore<UserToken> {
        return context.userToken
    }
}
