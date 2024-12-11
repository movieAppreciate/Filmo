package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintResponse

interface ComplaintRepository {
    suspend fun saveComplaint(saveComplaintRequest: SaveComplaintRequest): Result<SaveComplaintResponse>
}
