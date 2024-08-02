package com.teamfilmo.filmo.ui.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.movie.MovieInfo
import com.teamfilmo.filmo.databinding.UpcomingMovieItemBinding

class MovieInfoAdapter : RecyclerView.Adapter<MovieInfoAdapter.MovieInfoViewHolder>() {
    var movieList: ArrayList<MovieInfo> = arrayListOf()

    fun setMovieInfoList(
        movieInfoList: List<MovieInfo>,
    ) {
        val currentSize = movieList.size
        movieList.clear()
        movieList.addAll(movieInfoList)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, movieList.size)
    }

    inner class MovieInfoViewHolder(val binding: UpcomingMovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindMovieItem(item: MovieInfo) {
            val title = item.movieName
            val age = item.movieAge
            val genre = item.genres
            val image = item.movieImage

            binding.movieImage.setImageDrawable(image.toDrawable())
            binding.movieTitleTxt.text = title
            binding.ageTxt.text = age.toString()

            // todo : 추후 수정
            binding.genreTxt.text = genre.first().toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieInfoViewHolder {
        val binding = UpcomingMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
