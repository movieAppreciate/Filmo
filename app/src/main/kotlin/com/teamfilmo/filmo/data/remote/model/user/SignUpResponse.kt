package com.teamfilmo.filmo.data.remote.model.user

data class SignUpResponse(
    override val uid: String,
    override val userId: String,
    override val nickname: String,
    override val type: String,
    override val profileUrl: String? = null,
    override val lastLoginDate: String,
    override val introduction: String,
    override val role: List<String> = emptyList(),
) : UserResponse(uid, userId, nickname, type, profileUrl, lastLoginDate, introduction, role)
