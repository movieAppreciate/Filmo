package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintRequest

interface ComplaintDataSource {
    suspend fun saveComplaint(saveComplaintRequest: SaveComplaintRequest): Result<String>
}
