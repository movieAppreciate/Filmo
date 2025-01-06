package com.teamfilmo.filmo.data.remote.entity.block

import kotlinx.serialization.Serializable

@Serializable
data class SaveBlockRequest(
    val targetId: String,
)
