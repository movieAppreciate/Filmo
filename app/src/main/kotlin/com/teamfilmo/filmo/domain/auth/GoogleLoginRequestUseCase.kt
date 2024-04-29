package com.teamfilmo.filmo.domain.auth

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject

class GoogleLoginRequestUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(credential: Credential): Result<String> {
            val userId = getUserIdFromCredential(credential)
            return authRepository.login(userId, AuthType.GOOGLE.value)
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
