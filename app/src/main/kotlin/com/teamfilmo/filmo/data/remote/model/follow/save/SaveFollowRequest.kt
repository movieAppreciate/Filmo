package com.teamfilmo.filmo.data.remote.model.follow.save

import kotlinx.serialization.Serializable

@Serializable
data class SaveFollowRequest(
    val targetId: String,
)
