package com.teamfilmo.filmo.ui.auth

import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import com.teamfilmo.filmo.domain.MakeLoginRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val makeLoginRequestUseCase: MakeLoginRequestUseCase,
    ) : ViewModel() {
        suspend fun googleLogin(credential: Credential) {
            makeLoginRequestUseCase.invoke(credential)
        }
    }
