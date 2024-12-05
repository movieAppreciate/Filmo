package com.teamfilmo.filmo.data.remote.model.user

// 회원가입 후 로컬 db에 저장할 유저 정보
class UserInfo(
    val type: String = "",
    val userId: String = "",
    val nickName: String = "",
    val roles: String = " ",
)
