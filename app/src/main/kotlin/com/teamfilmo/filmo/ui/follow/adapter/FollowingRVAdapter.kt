package com.teamfilmo.filmo.ui.follow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.follow.MutualFollowUserInfo
import com.teamfilmo.filmo.databinding.FollowerItemBinding
import com.teamfilmo.filmo.databinding.FollowingItemBinding

// 내가 팔로우하지 않은 경우
const val IS_NOT_FOLLOW_IN_FOLLOWING = 0

// 내가 이미 팔로잉한 경우
const val IS_FOLLOW_IN_FOLLOWING = 1

class FollowingRVAdapter : PagingDataAdapter<MutualFollowUserInfo, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var followingItemBinding: FollowingItemBinding
    private lateinit var followerItemBinding: FollowerItemBinding

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<MutualFollowUserInfo>() {
                override fun areItemsTheSame(
                    oldItem: MutualFollowUserInfo,
                    newItem: MutualFollowUserInfo,
                ): Boolean =
                    oldItem.userId == newItem.userId

                override fun areContentsTheSame(
                    oldItem: MutualFollowUserInfo,
                    newItem: MutualFollowUserInfo,
                ): Boolean =
                    oldItem == newItem
            }
    }

    inner class FollowingViewHolder(
        val binding: FollowingItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MutualFollowUserInfo) {
            binding.txtUserName.text = item.nickname
        }
    }

    inner class FollowerViewHolder(
        val binding: FollowerItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MutualFollowUserInfo) {
            binding.txtUserName.text = item.nickname

            // 이미 해당 유저를 내가 팔로우하는 경우 팔로우 버튼 안보이게 하기
            if (item.isFollowing == true) {
                binding.btnFollow.visibility = View.GONE
            } else {
                binding.btnFollow.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            /*
          팔로우 버튼 : 내가 팔로우 하지 않는 계정
           1. 내 팔로워 중 내가 팔로우 하지 않는 계정
           2. 다른 유저 팔로워 중 내가 팔로우 하지 않는 계정
           3. 다른 유저 팔로잉 중 내가 팔로우 하지 않는 계정
             */
            IS_NOT_FOLLOW_IN_FOLLOWING -> {
                followerItemBinding =
                    FollowerItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                FollowerViewHolder(followerItemBinding)
            }

            /*
            취소 버튼 : 내가 팔로우 하는 계정
             1. 내 팔로잉 리스트
             2. 다른 유저 팔로잉 중 내가 팔로우한 계정
             */
            IS_FOLLOW_IN_FOLLOWING -> {
                followingItemBinding =
                    FollowingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FollowingViewHolder(followingItemBinding)
            }

//            /*
//            버튼 표시 안함
//            1. 내 팔로워
//            2. 다른 유저 팔로워 중 내가 팔로우한 계정
//             */
//            IS_MUTUAL_FOLLOW -> {
//                followingItemBinding =
//                    FollowingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                FollowingViewHolder(followingItemBinding)
//            }

            else -> {
                throw RuntimeException("알 수 없는 뷰 타입 에러")
            }
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        getItem(position).let {
            if (it != null) {
                if (holder is FollowingViewHolder) {
                    holder.bind(it)
                } else if (holder is FollowerViewHolder) {
                    holder.bind(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (getItem(position)?.isFollowing == true) {
            IS_FOLLOW_IN_FOLLOWING
        } else {
            IS_NOT_FOLLOW_IN_FOLLOWING
        }
}
