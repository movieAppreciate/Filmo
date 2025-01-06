package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintResponse
import com.teamfilmo.filmo.data.source.ComplaintDataSource
import com.teamfilmo.filmo.domain.repository.ComplaintRepository
import javax.inject.Inject

class ComplaintRepositoryImpl
    @Inject
    constructor(
        private val complaintDataSource: ComplaintDataSource,
    ) : ComplaintRepository {
        override suspend fun saveComplaint(saveComplaintRequest: SaveComplaintRequest): Result<SaveComplaintResponse> = complaintDataSource.saveComplaint(saveComplaintRequest)
    }
