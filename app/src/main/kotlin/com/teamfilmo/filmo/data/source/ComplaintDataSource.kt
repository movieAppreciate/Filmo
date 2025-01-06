package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintResponse

interface ComplaintDataSource {
    suspend fun saveComplaint(saveComplaintRequest: SaveComplaintRequest): Result<SaveComplaintResponse>
}
