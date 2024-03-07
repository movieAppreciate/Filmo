package com.teamfilmo.filmo.data.local.di

import com.teamfilmo.filmo.data.local.source.UserTokenSource
import com.teamfilmo.filmo.data.local.source.impl.UserTokenSourceImpl
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
    abstract fun bindUserTokenSource(localSourceImpl: UserTokenSourceImpl): UserTokenSource
}
