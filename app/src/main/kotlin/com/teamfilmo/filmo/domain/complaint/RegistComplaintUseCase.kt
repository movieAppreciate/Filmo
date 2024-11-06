package com.teamfilmo.filmo.domain.complaint

import com.teamfilmo.filmo.data.remote.model.complaint.RegistComplaintResponse
import com.teamfilmo.filmo.domain.repository.ComplaintRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RegistComplaintUseCase
    @Inject
    constructor(
        private val complaintRepository: ComplaintRepository,
    ) {
        operator fun invoke(reportId: String): Flow<RegistComplaintResponse?> =
            flow {
                val result = complaintRepository.registComplaint(reportId)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull())
            }.catch {
                Timber.d("failed to regist complaint : ${it.localizedMessage}")
            }
    }
