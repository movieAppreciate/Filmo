package com.teamfilmo.filmo.ui.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.databinding.ItemUpcomingMovieBinding
import com.teamfilmo.filmo.domain.model.movie.MovieInfo
import com.teamfilmo.filmo.ui.report.mapper.GenreMapper

class MovieInfoAdapter : RecyclerView.Adapter<MovieInfoAdapter.MovieInfoViewHolder>() {
    var movieList: ArrayList<MovieInfo> = arrayListOf()

    interface ItemClick {
        fun onClick(
            position: Int,
        )
    }

    var itemClick: ItemClick? = null

    fun setMovieInfoList(
        movieInfoList: List<MovieInfo>,
    ) {
        val currentSize = movieList.size
        movieList.clear()
        movieList.addAll(movieInfoList)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, movieList.size)
    }

    inner class MovieInfoViewHolder(
        val binding: ItemUpcomingMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindMovieItem(item: MovieInfo) {
            for (genre in item.genres) {
                genre
            }
            val title = item.movieName
            val genre = item.genres
            val image = item.movieImage

            Glide
                .with(binding.root.context)
                .asBitmap()
                .load(image)
                .into(binding.movieImage)

            binding.movieCardView.setOnClickListener {
                itemClick?.onClick(adapterPosition)
            }
            binding.movieTitleTxt.text = title

            val mutableList = mutableListOf<String>()
            // todo : 추후 수정
            for (i in genre) {
                mutableList.add(GenreMapper.getGenreName(i))
            }
            binding.genreTxt.text = mutableList.joinToString(", ")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieInfoViewHolder {
        val binding = ItemUpcomingMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(
        holder: MovieInfoViewHolder,
        position: Int,
    ) {
        holder.bindMovieItem(movieList[position])
    }
}
