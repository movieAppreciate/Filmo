package com.teamfilmo.filmo.data.remote.model.complaint

import kotlinx.serialization.Serializable

@Serializable
data class SaveComplaintResponse(
    val complaintId: String,
    val userid: String,
    val targetId: String,
    val type: String,
    val content: String,
)
