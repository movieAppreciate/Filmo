package com.teamfilmo.filmo.ui.follow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.follow.FollowUserInfo
import com.teamfilmo.filmo.databinding.FollowerItemBinding
import com.teamfilmo.filmo.databinding.FollowingItemBinding

const val FOLLOWER = 0
const val FOLLOWING = 1

class FollowRVAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val followers = ArrayList<FollowUserInfo>()
    private lateinit var followingItemBinding: FollowingItemBinding
    private lateinit var followerItemBinding: FollowerItemBinding
    private var viewType: Int = 0

    inner class FollowingViewHolder(
        val binding: FollowingItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FollowUserInfo) {
            binding.txtUserName.text = item.nickname
        }
    }

    inner class FollowerViewHolder(
        val binding: FollowerItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FollowUserInfo) {
            binding.txtUserName.text = item.nickname
        }
    }

    fun setViewType(viewType: Int) {
        this.viewType = viewType
    }

    fun setFollowers(newFollowers: List<FollowUserInfo>) {
        this.followers.clear()
        this.followers.addAll(newFollowers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            FOLLOWER -> {
                followerItemBinding =
                    FollowerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FollowerViewHolder(followerItemBinding)
            }

            FOLLOWING -> {
                followingItemBinding =
                    FollowingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FollowingViewHolder(followingItemBinding)
            }

            else -> {
                throw RuntimeException("알 수 없는 뷰 타입 에러")
            }
        }

    override fun getItemCount(): Int = followers.size

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        if (holder is FollowingViewHolder) {
            holder.bind(followers[position])
        } else if (holder is FollowerViewHolder) {
            holder.bind(followers[position])
        }
    }

    override fun getItemViewType(position: Int): Int = viewType
}
