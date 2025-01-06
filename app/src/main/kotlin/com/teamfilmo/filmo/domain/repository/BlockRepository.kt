package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockResponse

interface BlockRepository {
    suspend fun saveBlock(saveBlockRequest: SaveBlockRequest): Result<SaveBlockResponse>
}
