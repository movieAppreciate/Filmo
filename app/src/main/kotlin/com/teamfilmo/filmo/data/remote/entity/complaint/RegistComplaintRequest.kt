@file:Suppress("ktlint:standard:filename")

package com.teamfilmo.filmo.data.remote.entity.complaint

import kotlinx.serialization.Serializable

@Serializable
data class SaveComplaintRequest(
    val targetId: String,
    val type: String,
    val content: String? = null,
)
