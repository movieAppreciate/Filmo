package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItemWithRole
import com.teamfilmo.filmo.databinding.ItemReplyBinding
import com.teamfilmo.filmo.ui.reply.ReplyInteractionListener
import com.teamfilmo.filmo.ui.reply.SubReplyInteractionListener
import com.teamfilmo.filmo.ui.reply.SubReplyWithLikeInfo
import com.teamfilmo.filmo.ui.reply.diffutil.ReplyDiffCallBack
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ReplyRVAdapter : RecyclerView.Adapter<ReplyRVAdapter.ReplyViewHolder>() {
    val replyList: ArrayList<GetReplyResponseItemWithRole> = arrayListOf()
    var itemClick: ReplyInteractionListener? = null
    private var currentUserId: String = ""

    fun setUserId(userId: String) {
        this.currentUserId = userId
    }

    /*
    좋아요 등록
     */
    suspend fun updateLikeState(
        newList: List<GetReplyResponseItemWithRole>,
    ) {
        val diffCallBack = ReplyDiffCallBack(this.replyList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        replyList.clear()
        replyList.addAll(newList)
        withContext(Dispatchers.Main) {
            diffResult.dispatchUpdatesTo(this@ReplyRVAdapter)
        }
    }

    /*
    댓글 삭제
     */
    fun removeReplyItem(replyId: String) {
        val index = replyList.indexOfFirst { it.replyId == replyId }
        this.replyList.removeAt(index)
        notifyItemRemoved(index)
    }

    suspend fun setReplyList(replyList: List<GetReplyResponseItemWithRole>) {
        val diffCallBack = ReplyDiffCallBack(this.replyList, replyList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        this.replyList.clear()
        this.replyList.addAll(replyList)
        withContext(Dispatchers.Main) {
            diffResult.dispatchUpdatesTo(this@ReplyRVAdapter)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReplyViewHolder {
        val binding = ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(currentUserId, binding, itemClick)
    }

    override fun onBindViewHolder(
        holder: ReplyViewHolder,
        position: Int,
    ) {
        val reply = replyList[position]
        holder.bind(reply)
    }

    override fun getItemCount(): Int = replyList.size

    inner class ReplyViewHolder(
        private val currentUserId: String,
        private val binding: ItemReplyBinding,
        private val itemClick: ReplyInteractionListener?,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val subReplyAdapter =
            SubReplyRVAdapter().apply {
                subReplyListener =
                    object : SubReplyInteractionListener {
                        override fun onSubReplyLikeClick(
                            parentReplyId: String,
                            subReplyId: String,
                        ) {
                            Timber.d("답글 좋아요 클릭")
                            itemClick?.onSubReplyLikeClick(parentReplyId, subReplyId)
                        }

                        override fun onSubReplyMoreClick(
                            isMyReply: Boolean,
                            parentReplyId: String,
                            subReplyId: String,
                            userId: String,
                        ) {
                            itemClick?.onSubReplyMoreClick(isMyReply, parentReplyId, subReplyId, userId)
                        }
                    }
            }

        init {
            binding.apply {
                subReplyRecycerView.adapter = subReplyAdapter
                btnLike.setOnClickListener {
                    itemClick?.onReplyLikeClick(replyList[adapterPosition].replyId)
                }
                btnReply.setOnClickListener {
                    itemClick?.onReplyClick(adapterPosition)
                }

                // 전체 아이템 영역
                root.setOnClickListener {
                    itemClick?.onRootViewClick()
                }
            }
        }

        fun deleteSubReplyItem(
            upReplyId: String,
            subReplyId: String,
        ) {
            subReplyAdapter.deleteSubReplyItem(upReplyId, subReplyId)
        }

        fun updateSubReplyLikeState(
            subReplyId: String,
            isLiked: Boolean,
        ) {
            subReplyAdapter.updateItemLikeState(subReplyId, isLiked)
        }

        fun bind(reply: GetReplyResponseItemWithRole) {
            binding.txtReply.text = reply.content
            binding.txtReplyCount.text = reply.subReply?.size?.toString() ?: "0"

            binding.userId.text = reply.nickname
            binding.txtTime.text = formatTimeDifference(reply.createDate)

            binding.btnMore.setOnClickListener {
                if (reply.isMyReply) {
                    itemClick?.onReplyMoreClick(true, replyList[position].replyId, replyList[position].userId)
                } else {
                    itemClick?.onReplyMoreClick(false, replyList[position].replyId, replyList[position].userId)
                }
            }

            if (reply.isLiked) {
                binding.btnLike.setImageResource(R.drawable.ic_like_selected)
            } else {
                binding.btnLike.setImageResource(R.drawable.ic_like_unselected)
            }
            // 좋아요 수 반영
            binding.txtLikeCount.text = reply.likeCount.toString()

            Timber.d("답글 리스트 정보 :${reply.subReply}")
            val subReplyListWithRole =
                reply.subReply?.map {
                    SubReplyWithLikeInfo(
                        content = it.content,
                        createDate = it.createDate,
                        lastModifiedDate = it.lastModifiedDate,
                        nickname = it.nickname,
                        replyId = it.replyId,
                        reportId = it.reportId,
                        upReplyId = it.upReplyId,
                        userId = it.userId,
                        isLiked = it.isLiked,
                        likeCount = it.likeCount,
                        // 내가 작성한 댓글인지 알기 위해서 감상문의 userId와 현재 로그인한 유저의 userId를 비교
                        isMySubReply = it.userId == currentUserId,
                    )
                }
            reply.subReply?.let {
                if (subReplyListWithRole?.size == 0) {
                    subReplyAdapter.setSubReply(subReplyListWithRole)
                    binding.btnReplyMore.visibility = View.GONE
                    binding.btnCloseSubReply.visibility = View.GONE
                    return
                }

                binding.subReplyRecycerView.visibility = View.VISIBLE
                subReplyListWithRole?.let { it1 -> subReplyAdapter.setSubReply(it1.take(1)) }
                binding.btnReplyMore.visibility = View.GONE

                if (subReplyListWithRole?.size!! > 1) {
                    with(binding) {
                        btnReplyMore.apply {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                subReplyAdapter.setSubReply(subReplyListWithRole)
                                visibility = View.GONE
                                binding.btnCloseSubReply.visibility = View.VISIBLE
                            }
                        }

                        btnCloseSubReply.setOnClickListener {
                            subReplyListWithRole.take(1).let {
                                subReplyAdapter.setSubReply(it)
                            }
                            binding.btnCloseSubReply.visibility = View.GONE
                            binding.btnReplyMore.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}

private fun formatTimeDifference(dateString: String): String {
    val possiblePatterns =
        listOf(
            "MMM d, yyyy, h:mm:ss a",
            "MMM dd, yyyy, h:mm:ss a",
            "MMM d, yyyy, hh:mm:ss a",
            "MMM dd, yyyy, hh:mm:ss a",
        )
    var inputDate: Date? = null

    for (pattern in possiblePatterns) {
        try {
            val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
            formatter.isLenient = false
            inputDate = formatter.parse(dateString)
            break
        } catch (e: ParseException) {
            continue
        }
    }

    if (inputDate == null) {
        return "Invalid Date"
    }

    val currentDate = Date()
    val diffInMills = currentDate.time - inputDate.time
    val diffInSeconds = abs(diffInMills / 1000)
    val diffinMinutes = diffInSeconds / 60
    val diffiInHours = diffinMinutes / 60

    return when {
        diffInSeconds < 60 -> "${diffInSeconds}초 전"
        diffinMinutes < 60 -> "${diffinMinutes}분 전"
        diffiInHours < 24 -> "${diffiInHours}시간 전"
        else -> {
            val outputFormatter = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
            outputFormatter.format(inputDate)
        }
    }
}
