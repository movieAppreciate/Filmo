package com.teamfilmo.filmo.app.di

import android.content.Context
import com.teamfilmo.filmo.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {
    @Provides
    @Singleton
    fun provideTimerTree(
        @ApplicationContext context: Context,
    ): Timber.Tree =
        object : Timber.DebugTree() {
            private val tag = context.getString(R.string.app_name)

            override fun createStackElementTag(
                element: StackTraceElement,
            ): String = "(${element.fileName}:${element.lineNumber})#${element.methodName}"

            override fun log(
                priority: Int,
                tag: String?,
                message: String,
                t: Throwable?,
            ) {
                super.log(priority, this.tag, "$tag - $message", t)
            }
        }
}
