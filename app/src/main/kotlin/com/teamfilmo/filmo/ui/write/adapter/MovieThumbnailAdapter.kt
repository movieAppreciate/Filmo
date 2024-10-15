package com.teamfilmo.filmo.ui.write.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.databinding.MovieBackgroundItemBinding
import com.teamfilmo.filmo.databinding.MoviePosterItemBinding
import timber.log.Timber

class MovieThumbnailAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var posterUriList: MutableList<String> = arrayListOf()
    private var selectedPosition: Int? = null
    private var viewType = 0

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_BACKGROUND = 2
    }

    fun setViewType(viewType: Int) {
        this.viewType = viewType
        notifyDataSetChanged()
    }

    fun setPosterUriList(uriList: List<String>) {
        posterUriList.clear()
        posterUriList.addAll(uriList)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            uri: String?,
        )
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return if (viewType == 2) {
            VIEW_TYPE_BACKGROUND
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = MoviePosterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MoviePosterViewHolder(binding)
            }
            VIEW_TYPE_BACKGROUND -> {
                val binding = MovieBackgroundItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieBackgroundViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = posterUriList.size

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (holder) {
            is MoviePosterViewHolder -> {
                holder.bind()
            }
            is MovieBackgroundViewHolder -> {
                holder.bind()
            }
            else -> {}
        }
    }

    inner class MoviePosterViewHolder(private val binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivMoviePoster.setOnClickListener {
                Timber.d("clicked : $position")
                selectedPosition = position
                onItemClickListener?.onItemClick(position, posterUriList[position])
            }
        }

        fun bind() {
            Glide.with(context)
                .load(posterUriList[position])
                .into(binding.ivMoviePoster)
        }
    }

    inner class MovieBackgroundViewHolder(private val binding: MovieBackgroundItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // 이미지 클릭 이벤트
        init {
            binding.ivMovieBackground.setOnClickListener {
                Timber.d("clicked : $position")
                selectedPosition = position
                onItemClickListener?.onItemClick(position, posterUriList[position])
            }
        }

        fun bind() {
            Glide.with(context)
                .load(posterUriList[position])
                .into(binding.ivMovieBackground)
        }
    }
}
