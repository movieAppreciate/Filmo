package com.teamfilmo.filmo.ui.auth

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.domain.MakeGoogleLoginRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val makeGoogleLoginRequestUseCase: MakeGoogleLoginRequestUseCase,
    ) : ViewModel() {
        @Inject
        lateinit var credentialRequest: GetCredentialRequest

        fun requestGoogleLogin(context: Context) {
            val credentialManager = CredentialManager.create(context)
            viewModelScope.launch {
                runCatching {
                    credentialManager.getCredential(
                        request = credentialRequest,
                        context = context,
                    )
                }
                    .onSuccess {
                        val credential = it.credential
                        googleLogin(credential)
                    }
                    .onFailure {
                        Log.d("google login failed", it.message.toString())
                    }
            }
        }

        private fun googleLogin(credential: Credential) {
            viewModelScope.launch {
                makeGoogleLoginRequestUseCase(credential)
            }
        }
    }
