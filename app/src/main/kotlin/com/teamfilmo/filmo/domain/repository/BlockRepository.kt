package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.model.block.SaveBlockResponse

interface BlockRepository {
    suspend fun saveBlock(saveBlockRequest: SaveBlockRequest): Result<SaveBlockResponse>
}
