package com.teamfilmo.filmo.data.remote.model.block

import kotlinx.serialization.Serializable

@Serializable
data class SaveBlockResponse(
    val blockId: String? = null,
    val createDate: String? = null,
    val lastModifiedDate: String? = null,
    val targetId: String? = null,
    val userId: String? = null,
)
