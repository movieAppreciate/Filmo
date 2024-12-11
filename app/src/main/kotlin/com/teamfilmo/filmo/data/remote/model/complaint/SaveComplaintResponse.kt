package com.teamfilmo.filmo.data.remote.model.complaint

import kotlinx.serialization.Serializable

@Serializable
data class SaveComplaintResponse(
    val complaintId: String? = null,
)
