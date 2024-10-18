package com.teamfilmo.filmo.domain.user

import com.teamfilmo.filmo.data.remote.model.user.UserResponse
import com.teamfilmo.filmo.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserInfoUseCase
    @Inject
    constructor(private val userRepository: UserRepository) {
        operator fun invoke(userId: String? = null): Flow<UserResponse?> =
            flow {
                val result =
                    userRepository.getUserInfo(
                        userId,
                    )
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull())
            }
    }
