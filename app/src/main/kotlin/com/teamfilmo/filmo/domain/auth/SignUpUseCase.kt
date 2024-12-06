package com.teamfilmo.filmo.domain.auth

import com.google.gson.Gson
import com.teamfilmo.filmo.data.remote.model.user.ExistingUserResponse
import com.teamfilmo.filmo.data.remote.model.user.SignUpRequest
import com.teamfilmo.filmo.data.remote.model.user.SignUpState
import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SignUpUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        operator fun invoke(signUpRequest: SignUpRequest): Flow<SignUpState?> =
            flow {
                val result = authRepository.signUp(signUpRequest)
                val gson = Gson()
                result
                    .onSuccess {
                        Timber.d("sign up use case : $it")
                        when (it.code) {
                            200 -> {
                                it.data.let {
                                    if (it != null) emit(SignUpState.Success(it))
                                }
                            }
                            202 -> {
                                val existingUser =
                                    gson.fromJson(
                                        gson.toJson(it.data),
                                        ExistingUserResponse::class.java,
                                    )
                                emit(SignUpState.Existing(existingUser))
                            }
                        }
                    }.onFailure {
                        Timber.e("failed to sign up :${it.localizedMessage}")
                        emit(null)
                    }
            }
    }
