package com.teamfilmo.filmo.ui.follow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.follow.MutualFollowUserInfo
import com.teamfilmo.filmo.databinding.FollowerItemBinding
import com.teamfilmo.filmo.ui.follow.adapter.FollowerRVAdapter.FollowerViewHolder

// 나를 팔로잉 , 나는 팔로잉 x

class FollowerRVAdapter : PagingDataAdapter<MutualFollowUserInfo, FollowerViewHolder>(DIFF_CALLBACK) {
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
    ): FollowerViewHolder {
        followerItemBinding = FollowerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowerViewHolder(followerItemBinding)
    }

    override fun onBindViewHolder(
        holder: FollowerViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { holder.bind(it) }
    }
}
