package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.complaint.RegistComplaintResponse
import com.teamfilmo.filmo.data.remote.service.ComplaintService
import com.teamfilmo.filmo.data.source.ComplaintDataSource
import javax.inject.Inject

class ComplaintDataSourceImpl
    @Inject
    constructor(
        private val complaintService: ComplaintService,
    ) : ComplaintDataSource {
        override suspend fun registComplaint(reportId: String): Result<RegistComplaintResponse> = complaintService.registComplaint(reportId)
    }
