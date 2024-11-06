package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.complaint.RegistComplaintResponse

interface ComplaintDataSource {
    suspend fun registComplaint(reportId: String): Result<RegistComplaintResponse>
}
