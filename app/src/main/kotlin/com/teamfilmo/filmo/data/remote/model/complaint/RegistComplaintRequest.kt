package com.teamfilmo.filmo.data.remote.model.complaint

import kotlinx.serialization.Serializable

@Serializable
data class RegistComplaintRequest(
    val reportId: String,
)
