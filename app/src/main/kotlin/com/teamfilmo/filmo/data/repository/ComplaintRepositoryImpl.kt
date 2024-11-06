package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.complaint.RegistComplaintResponse
import com.teamfilmo.filmo.data.source.ComplaintDataSource
import com.teamfilmo.filmo.domain.repository.ComplaintRepository
import javax.inject.Inject

class ComplaintRepositoryImpl
    @Inject
    constructor(
        private val complaintDataSource: ComplaintDataSource,
    ) : ComplaintRepository {
        override suspend fun registComplaint(reportId: String): Result<RegistComplaintResponse> = complaintDataSource.registComplaint(reportId)
    }
