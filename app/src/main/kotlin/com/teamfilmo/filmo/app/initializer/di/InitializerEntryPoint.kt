package com.teamfilmo.filmo.app.initializer.di

import android.content.Context
import com.teamfilmo.filmo.app.initializer.TimberInitializer
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerEntryPoint {
    companion object {
        fun resolve(context: Context): InitializerEntryPoint {
            val applicationContext = context.applicationContext
            return EntryPointAccessors.fromApplication(applicationContext, InitializerEntryPoint::class.java)
        }
    }

    fun inject(initializer: TimberInitializer)
}
