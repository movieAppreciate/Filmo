package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.complaint.RegistComplaintResponse

interface ComplaintRepository {
    suspend fun registComplaint(reportId: String): Result<RegistComplaintResponse>
}
