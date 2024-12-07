package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
sealed class SignUpResult {
    data class Success(
        val response: SignUpResponse,
    ) : SignUpResult()

    data class Existing(
        val response: ExistingUserResponse,
    ) : SignUpResult()

    data class Error(
        val message: String,
    ) : SignUpResult()
}
