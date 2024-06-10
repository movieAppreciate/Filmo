package com.teamfilmo.filmo.data.remote.model.follow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FollowResponse(
    @SerialName("followId")
    val id: String,
    val userId: String,
    val followTarget: String,
)
