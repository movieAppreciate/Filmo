package com.teamfilmo.filmo.domain.user

import com.teamfilmo.filmo.data.remote.entity.user.delete.DeleteUserResponse
import com.teamfilmo.filmo.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteUserUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) {
        operator fun invoke(userId: String): Flow<DeleteUserResponse?> =
            flow {
                userRepository
                    .quitUser(userId)
                    .onSuccess {
                        emit(it)
                    }.onFailure {
                        emit(null)
                    }
            }
    }
