package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class ExistingUserResponse(
    val msg: String,
    val email: String,
    val type: String,
    val dup: String,
)

@Serializable
data class SignUpResult(
    val code: Int, // HTTP 상태 코드
    val data: SignUpResponse?, // 실제 데이터
    val message: String?,
)

sealed class SignUpState {
    data class Success(
        val data: SignUpResponse,
    ) : SignUpState()

    data class Existing(
        val data: ExistingUserResponse?,
    ) : SignUpState()
}
