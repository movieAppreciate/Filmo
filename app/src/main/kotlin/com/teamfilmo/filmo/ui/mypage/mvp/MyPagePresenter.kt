package com.teamfilmo.filmo.ui.mypage.mvp

import com.teamfilmo.filmo.data.remote.model.user.UserInfo
import com.teamfilmo.filmo.domain.follow.CountFollowUseCase
import com.teamfilmo.filmo.domain.user.GetUserInfoUseCase
import com.teamfilmo.filmo.ui.mypage.MyPageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPagePresenter(
    private val myPageFragment: MyPageFragment,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val countFollowUseCase: CountFollowUseCase,
) : MyPageContract.Presenter {
    override fun onFollowButtonClick() {
        myPageFragment.navigateToFollowerFragment()
    }

    override fun onFollowingButtonClick() {
        myPageFragment.navigateToFollowingFragment()
    }

    override fun onSettingButtonClick() {
        myPageFragment.navigateToSettingFragment()
    }

    override fun getUserInfo() {
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                // 내 정보 가져오기
                getUserInfoUseCase().collect {
                    if (it != null) {
                        myPageFragment.displayUserInfo(
                            UserInfo(
                                userId = it.userId,
                                nickName = it.nickname,
                                roles = it.roles,
                            ),
                        )
                    }
                }
            }

            launch {
                countFollowUseCase(null).collect {
                    if (it != null) {
                        myPageFragment.showFollowInfo(it)
                    }
                }
            }
        }
    }
}
