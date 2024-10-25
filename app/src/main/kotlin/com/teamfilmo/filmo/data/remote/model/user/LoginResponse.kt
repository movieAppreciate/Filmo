package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

// sealed class LoginResponse {
@Serializable
data class LoginResponse(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
)

//    @Serializable
//    data class LoginFailureResponse(
// val timeStamp : String,
//    val status : Int,
//        val error : String,
//            val message : String,
//                val path : String
// )
//    )
