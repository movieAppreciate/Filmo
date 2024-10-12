package com.teamfilmo.filmo.ui.write.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.databinding.ItemLoadingBinding
import com.teamfilmo.filmo.databinding.MovieBackgroundItemBinding
import com.teamfilmo.filmo.databinding.MoviePosterItemBinding
import com.teamfilmo.filmo.ui.write.paging.MovieContentResultWithIndex
import timber.log.Timber

/*
RecyclerView를 통해 Paging한 데이터를 보여주기 위해서 PagingDataAdapter를 확장해준다.
PagingDataAdapter를 확장하여 MoviePosterAdapter 어댑터를 만들어 MovieContentResultWithIndex 타입의 목록에 대한
RecyclerView 어댑터를 제공하고  MoviePosterViewHolder ,MovieBackgroundViewHolder를 뷰홀더로 사용하고 있다.

 */
class MoviePosterAdapter(private val context: Context) : PagingDataAdapter<MovieContentResultWithIndex, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    /*
    DiffUtil.ItemCallback 지정
     */
    private class MovieDiffCallback : DiffUtil.ItemCallback<MovieContentResultWithIndex>() {
        override fun areItemsTheSame(
            oldItem: MovieContentResultWithIndex,
            newItem: MovieContentResultWithIndex,
        ): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(
            oldItem: MovieContentResultWithIndex,
            newItem: MovieContentResultWithIndex,
        ): Boolean {
            return oldItem.index == newItem.index
        }
    }

    private var selectedPosition: Int? = null
    private var viewType = 0

    companion object {
        const val VIEW_TYPE_ITEM = 0

//        const val VIEW_TYPE_LOADING = 1
        const val VIEW_TYPE_BACKGROUND = 2
    }

    fun setViewType(viewType: Int) {
        this.viewType = viewType
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

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val moviePath = getItem(position)
        Timber.d("position : $position")
        Timber.d("movie : $moviePath")
        if (moviePath != null) {
            when (holder) {
                is MoviePosterViewHolder -> {
                    holder.bind(moviePath)
                }

                is MovieBackgroundViewHolder -> {
                    holder.bind(moviePath)
                }
                else -> {}
            }
        }
    }

    inner class MoviePosterViewHolder(private val binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivMoviePoster.setOnClickListener {
                Timber.d("clicked : $position")
                selectedPosition = position
            }
        }

        fun bind(movie: MovieContentResultWithIndex) {
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/original${movie.result.posterPath}")
                .into(binding.ivMoviePoster)
//            binding.root.setOnClickListener { on(movie) }
        }
    }

    inner class MovieBackgroundViewHolder(private val binding: MovieBackgroundItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // 이미지 클릭 이벤트
        init {
            binding.ivMovieBackground.setOnClickListener {
                Timber.d("clicked : $position")
                selectedPosition = position
//                onItemClickListener?.onItemClick(position, posterUriList[position])
            }
        }

        fun bind(movie: MovieContentResultWithIndex) {
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/original${movie.result.posterPath}")
                .into(binding.ivMovieBackground)
//            binding.root.setOnClickListener { on(movie) }
        }
    }

    inner class MovieLoadingViewHolder(private val binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}
