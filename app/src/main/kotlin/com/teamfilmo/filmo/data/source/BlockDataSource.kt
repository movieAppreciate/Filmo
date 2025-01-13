package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockResponse

interface BlockDataSource {
    suspend fun saveBlock(saveBlockRequest: SaveBlockRequest): Result<SaveBlockResponse>
}
