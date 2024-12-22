package com.teamfilmo.filmo.ui.mypage.mvp

interface MyPageContract {
    interface View {
        // 뷰에서 UI 업데이트를 하기 위한 메서드를 선언
        fun navigateToSettingFragment()

        fun navigateToFollowerFragment()

        fun navigateToFollowingFragment()
    }

    interface Presenter {
        // UI 비즈니스 로직을 처리하기 위한 메소드 선언
        fun onFollowButtonClick()

        fun onFollowingButtonClick()

        fun onSettingButtonClick()

        fun getUserInfo()
    }
}
