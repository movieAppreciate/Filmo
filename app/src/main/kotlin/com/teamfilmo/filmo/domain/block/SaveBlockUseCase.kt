package com.teamfilmo.filmo.domain.block

import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockResponse
import com.teamfilmo.filmo.domain.repository.BlockRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class SaveBlockUseCase
    @Inject
    constructor(
        private val blockRepository: BlockRepository,
    ) {
        operator fun invoke(saveBlockRequest: SaveBlockRequest): Flow<SaveBlockResponse?> =
            flow {
                val result = blockRepository.saveBlock(saveBlockRequest)
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
                emit(result.getOrThrow())
            }.catch {
                Timber.d("Failed to save block")
            }
    }
