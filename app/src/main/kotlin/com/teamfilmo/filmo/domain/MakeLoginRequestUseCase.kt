package com.teamfilmo.filmo.domain

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.teamfilmo.filmo.data.repository.AuthRepository
import javax.inject.Inject

class MakeLoginRequestUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(credential: Credential) {
            val userId = getUserIdFromCredential(credential)
            val loginResult = authRepository.login(userId, "google")

            if (loginResult.isFailure) {
                authRepository.signUp(userId, "google", "")
            }
        }

        private fun getUserIdFromCredential(credential: Credential): String {
            when (credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)
                            return googleIdTokenCredential.id
                        } catch (e: GoogleIdTokenParsingException) {
                            throw IllegalStateException("Failed to get Google ID Token Credential", e)
                        }
                    } else {
                        throw IllegalArgumentException("Unexpected credential type: ${credential.type}")
                    }
                }
                else -> throw IllegalArgumentException("Unsupported credential type")
            }
        }
    }
