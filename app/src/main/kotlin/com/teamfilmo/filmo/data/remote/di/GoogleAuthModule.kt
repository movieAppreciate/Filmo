package com.teamfilmo.filmo.data.remote.di

import android.content.Context
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.teamfilmo.filmo.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object GoogleAuthModule {
    @Provides
    @ViewModelScoped
    fun provideGoogleIdOption(
        @ApplicationContext context: Context,
    ): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.google_client_key))
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideCredentialRequest(
        @ApplicationContext context: Context,
    ): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(provideGoogleIdOption(context))
            .build()
    }
}
