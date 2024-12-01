package com.teamfilmo.filmo.domain.user

import com.teamfilmo.filmo.data.remote.model.user.UserQuitRequest
import com.teamfilmo.filmo.data.remote.model.user.UserQuitResponse
import com.teamfilmo.filmo.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteUserUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) {
        operator fun invoke(userId: String): Flow<UserQuitResponse?> =
            flow {
                userRepository
                    .quitUser(UserQuitRequest(userId))
                    .onSuccess {
                        emit(it)
                    }.onFailure {
                        emit(null)
                    }
            }
    }
