package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface BlockService {
    @POST("/block/save")
    suspend fun saveBlock(
        @Body saveBlockRequest: SaveBlockRequest,
    ): Result<SaveBlockResponse>
}
