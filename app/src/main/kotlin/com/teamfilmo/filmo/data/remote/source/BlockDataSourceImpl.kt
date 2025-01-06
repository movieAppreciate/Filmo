package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockResponse
import com.teamfilmo.filmo.data.remote.service.BlockService
import com.teamfilmo.filmo.data.source.BlockDataSource
import javax.inject.Inject

class BlockDataSourceImpl
    @Inject
    constructor(
        private val blockService: BlockService,
    ) : BlockDataSource {
        override suspend fun saveBlock(saveBlockRequest: SaveBlockRequest): Result<SaveBlockResponse> = blockService.saveBlock(saveBlockRequest)
    }
