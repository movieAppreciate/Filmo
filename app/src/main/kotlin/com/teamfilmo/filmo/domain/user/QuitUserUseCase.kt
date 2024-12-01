package com.teamfilmo.filmo.domain.user

import com.teamfilmo.filmo.data.remote.model.user.UserQuitRequest
import com.teamfilmo.filmo.data.remote.model.user.UserQuitResponse
import com.teamfilmo.filmo.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class QuitUserUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) {
        operator fun invoke(userId: String): Flow<UserQuitResponse> =
            flow {
                val result = userRepository.quitUser(UserQuitRequest(userId))
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull() ?: UserQuitResponse())
            }.catch {
                Timber.d("failed to quit user :${it.localizedMessage}")
            }
    }
