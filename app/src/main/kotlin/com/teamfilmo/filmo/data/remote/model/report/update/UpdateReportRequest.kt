package com.teamfilmo.filmo.data.remote.model.report.update

import com.teamfilmo.filmo.data.remote.model.report.ReportRequest

data class UpdateReportRequest(
    val reportId: String,
    override val title: String,
    override val content: String,
    override val movieId: String,
    override val imageUri: String,
    override val tagString: String,
) : ReportRequest(title, content, movieId, imageUri, tagString)
