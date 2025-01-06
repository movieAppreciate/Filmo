package com.teamfilmo.filmo.data.remote.entity.complaint

import kotlinx.serialization.Serializable

@Serializable
data class SaveComplaintResponse(
    val complaintId: String? = null,
)
