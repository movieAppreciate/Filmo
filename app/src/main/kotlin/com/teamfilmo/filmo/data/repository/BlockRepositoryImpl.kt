package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.model.block.SaveBlockResponse
import com.teamfilmo.filmo.data.source.BlockDataSource
import com.teamfilmo.filmo.domain.repository.BlockRepository
import javax.inject.Inject

class BlockRepositoryImpl
    @Inject
    constructor(
        private val blockDataSource: BlockDataSource,
    ) : BlockRepository {
        override suspend fun saveBlock(saveBlockRequest: SaveBlockRequest): Result<SaveBlockResponse> = blockDataSource.saveBlock(saveBlockRequest)
    }
