package com.teamfilmo.filmo.data.remote.model.block

import kotlinx.serialization.Serializable

@Serializable
data class SaveBlockRequest(
    val targetId: String,
)
