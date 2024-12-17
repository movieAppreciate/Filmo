package com.teamfilmo.filmo.domain.complaint

import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintResponse
import com.teamfilmo.filmo.domain.repository.ComplaintRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class SaveComplaintUseCase
    @Inject
    constructor(
        private val complaintRepository: ComplaintRepository,
    ) {
        operator fun invoke(saveComplaintRequest: SaveComplaintRequest): Flow<SaveComplaintResponse?> =
            flow {
                val result = complaintRepository.saveComplaint(saveComplaintRequest)
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
                emit(result.getOrNull())
            }
    }
