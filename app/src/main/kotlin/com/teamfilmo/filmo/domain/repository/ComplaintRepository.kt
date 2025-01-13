package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintResponse

interface ComplaintRepository {
    suspend fun saveComplaint(saveComplaintRequest: SaveComplaintRequest): Result<SaveComplaintResponse>
}
