package com.teamfilmo.filmo.domain.block

import com.teamfilmo.filmo.data.remote.model.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.model.block.SaveBlockResponse
import com.teamfilmo.filmo.domain.repository.BlockRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SaveBlockUseCase
    @Inject
    constructor(
        private val blockRepository: BlockRepository,
    ) {
        operator fun invoke(saveBlockRequest: SaveBlockRequest): Flow<SaveBlockResponse> =
            flow {
                val result = blockRepository.saveBlock(saveBlockRequest)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull()!!)
            }.catch {
                Timber.d("Failed to save block")
            }
    }
