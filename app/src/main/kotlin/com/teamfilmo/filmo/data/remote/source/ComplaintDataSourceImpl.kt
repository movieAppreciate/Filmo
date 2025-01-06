package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintResponse
import com.teamfilmo.filmo.data.remote.service.ComplaintService
import com.teamfilmo.filmo.data.source.ComplaintDataSource
import javax.inject.Inject

class ComplaintDataSourceImpl
    @Inject
    constructor(
        private val complaintService: ComplaintService,
    ) : ComplaintDataSource {
        override suspend fun saveComplaint(saveComplaintRequest: SaveComplaintRequest): Result<SaveComplaintResponse> = complaintService.saveComplaint(saveComplaintRequest)
    }
