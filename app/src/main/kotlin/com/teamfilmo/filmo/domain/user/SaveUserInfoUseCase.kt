package com.teamfilmo.filmo.domain.user

import com.teamfilmo.filmo.domain.model.user.UserInfo
import com.teamfilmo.filmo.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveUserInfoUseCase
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke(userInfo: UserInfo) {
            userPreferencesRepository.saveUserInfo(userInfo)
        }
    }
