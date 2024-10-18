package com.teamfilmo.filmo.data.remote.model.report

import kotlinx.serialization.Serializable

@Serializable
open class ReportRequest(
    open val title: String,
    open val content: String,
    open val movieId: String,
    open val imageUri: String,
    open val tagString: String,
)
