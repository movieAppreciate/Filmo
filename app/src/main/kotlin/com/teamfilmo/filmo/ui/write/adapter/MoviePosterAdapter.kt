package com.teamfilmo.filmo.ui.write.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.databinding.MoviePosterItemBinding
import com.teamfilmo.filmo.ui.write.select.paging.MovieContentResultWithIndex
import timber.log.Timber

/*
RecyclerView를 통해 Paging한 데이터를 보여주기 위해서 PagingDataAdapter를 확장해준다.
PagingDataAdapter를 확장하여 MoviePosterAdapter 어댑터를 만들어 MovieContentResultWithIndex 타입의 목록에 대한
RecyclerView 어댑터를 제공하고  MoviePosterViewHolder ,MovieBackgroundViewHolder를 뷰홀더로 사용하고 있다.

 */
class MoviePosterAdapter(private val context: Context) : PagingDataAdapter<MovieContentResultWithIndex, MoviePosterAdapter.MoviePosterViewHolder>(MovieDiffCallback()) {
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

    interface OnItemClickListener {
        fun onItemClick(
            movieId: Int,
            movieName: String,
            uri: String,
        )
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MoviePosterViewHolder {
        val binding = MoviePosterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviePosterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MoviePosterViewHolder,
        position: Int,
    ) {
        val moviePath = getItem(position)
        Timber.d("position : $position")
        Timber.d("movie : $moviePath")
        if (moviePath != null) {
            holder.bind(moviePath)
        }
    }

    inner class MoviePosterViewHolder(private val binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivMoviePoster.setOnClickListener {
                Timber.d("clicked : $position")
                val movieId = getItem(position)?.result?.id
                val movieName = getItem(position)?.result?.title
                if (movieId != null && movieName != null) {
                    onItemClickListener?.onItemClick(movieId, movieName, "")
                }
            }
        }

        fun bind(movie: MovieContentResultWithIndex) {
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/original${movie.result.posterPath}")
                .into(binding.ivMoviePoster)
        }
    }
}
