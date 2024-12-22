package com.teamfilmo.filmo.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.teamfilmo.filmo.data.remote.model.follow.count.FollowCountResponse
import com.teamfilmo.filmo.data.remote.model.user.UserInfo
import com.teamfilmo.filmo.databinding.FragmentMyPageBinding
import com.teamfilmo.filmo.domain.follow.CountFollowUseCase
import com.teamfilmo.filmo.domain.user.GetUserInfoUseCase
import com.teamfilmo.filmo.ui.mypage.mvp.MyPageContract
import com.teamfilmo.filmo.ui.mypage.mvp.MyPagePresenter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// MVC : View : 화면 처리
@AndroidEntryPoint
class MyPageFragment :
    Fragment(),
    MyPageContract.View {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private var presenter: MyPagePresenter? = null

    @Inject
    lateinit var getUserInfoUseCase: GetUserInfoUseCase

    @Inject
    lateinit var countFollowUseCase: CountFollowUseCase

    private val navController by lazy { findNavController() }
    //  private val viewModel: MyPageViewModel by viewModels()

    private var userInfo: UserInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMyPageBinding.inflate(layoutInflater) // 바인딩 초기화

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        presenter = MyPagePresenter(this@MyPageFragment, getUserInfoUseCase, countFollowUseCase)
        presenter?.getUserInfo()

        with(binding) {
            btnSetting.setOnClickListener {
                navigateToSettingFragment()
            }

            btnBack.setOnClickListener {
                navController.popBackStack()
            }

            txtCountFollow.setOnClickListener {
                navigateToFollowerFragment()
            }

            txtCountFollowing.setOnClickListener {
                navigateToFollowingFragment()
            }
        }

//            viewLifecycleOwner.lifecycleScope.launch {
//                launch {
//                    viewModel.followInfo.collect {
//                        binding.txtCountFollow.text = it.countFollower.toString()
//                        binding.txtCountFollowing.text = it.countFollowing.toString()
//                    }
//                }
//                launch {
//                    viewModel.userInfo.collect {
//                        binding.txtUserName.text = it.nickName
//                    }
//                }
//            }
//            with(binding) {
//                btnSetting.setOnClickListener {
//                    val action = MyPageFragmentDirections.navigateToSettingFromMyPage(viewModel.userInfo.value.nickName)
//                    navController.navigate(action)
//                }
//
//                btnBack.setOnClickListener {
//                    navController.popBackStack()
//                }
//
//                txtCountFollow.setOnClickListener {
//                    val action =
//                        MyPageFragmentDirections.navigateToFollowFragment(
//                            position = 0,
//                            userId = viewModel.userInfo.value.userId,
//                        )
//                    navController.navigate(action)
//                }
//
//                txtCountFollowing.setOnClickListener {
//                    val action =
//                        MyPageFragmentDirections.navigateToFollowFragment(
//                            position = 1,
//                            userId = viewModel.userInfo.value.userId,
//                        )
//                    navController.navigate(action)
//                }
//            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // MyPageView 재정의
    fun displayUserInfo(userInfo: UserInfo) {
        this.userInfo = userInfo
        binding.txtUserName.text = userInfo.nickName
    }

    fun showFollowInfo(followInfo: FollowCountResponse) {
        binding.txtCountFollow.text = followInfo.countFollower.toString()
        binding.txtCountFollowing.text = followInfo.countFollowing.toString()
    }

    override fun navigateToSettingFragment() {
        if (userInfo != null) {
            val action = MyPageFragmentDirections.navigateToSettingFromMyPage(userInfo!!.nickName)
            navController.navigate(action)
        }
    }

    override fun navigateToFollowingFragment() {
        val action =
            MyPageFragmentDirections.navigateToFollowFragment(
                position = 1,
                userId = userInfo?.userId,
            )
        navController.navigate(action)
    }

    override fun navigateToFollowerFragment() {
        val action =
            MyPageFragmentDirections.navigateToFollowFragment(
                position = 0,
                userId = userInfo?.userId,
            )
        navController.navigate(action)
    }
}
