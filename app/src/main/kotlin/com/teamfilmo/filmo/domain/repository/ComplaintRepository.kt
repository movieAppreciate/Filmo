package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintRequest

interface ComplaintRepository {
    suspend fun saveComplaint(saveComplaintRequest: SaveComplaintRequest): Result<String>
}
