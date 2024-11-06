package com.teamfilmo.filmo.data.remote.model.complaint

import kotlinx.serialization.Serializable

@Serializable
data class RegistComplaintResponse(
    val complaintId: String,
    val userid: String,
    val targetid: String,
    val type: String,
    val content: String,
)
