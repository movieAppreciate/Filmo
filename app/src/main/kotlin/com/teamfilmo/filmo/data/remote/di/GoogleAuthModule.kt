package com.teamfilmo.filmo.data.remote.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.teamfilmo.filmo.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object GoogleAuthModule {
    @Provides
    @ActivityScoped
    fun provideGoogleIdOption(): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_API_KEY)
            .build()
    }

    @Provides
    @ActivityScoped
    fun provideCredentialRequest(): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(provideGoogleIdOption())
            .build()
    }

    @Provides
    @ActivityScoped
    fun provideCredentialManager(
        @ActivityContext context: Context,
    ): CredentialManager {
        return CredentialManager.create(context)
    }
}
