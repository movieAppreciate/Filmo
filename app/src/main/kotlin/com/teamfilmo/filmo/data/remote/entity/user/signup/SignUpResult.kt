package com.teamfilmo.filmo.data.remote.entity.user.signup

import com.teamfilmo.filmo.data.remote.entity.user.ExistingUserResponse
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
