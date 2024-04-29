package com.teamfilmo.filmo.data.local.di

import com.teamfilmo.filmo.data.local.source.UserTokenSourceImpl
import com.teamfilmo.filmo.data.source.UserTokenSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalSourceModule {
    @Binds
    @Singleton
    abstract fun bindUserTokenSource(
        localSourceImpl: UserTokenSourceImpl,
    ): UserTokenSource
}
