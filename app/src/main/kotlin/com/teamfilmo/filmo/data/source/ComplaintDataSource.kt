package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintResponse

interface ComplaintDataSource {
    suspend fun saveComplaint(saveComplaintRequest: SaveComplaintRequest): Result<SaveComplaintResponse>
}
