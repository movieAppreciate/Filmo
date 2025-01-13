package com.teamfilmo.filmo.ui.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.databinding.ItemUpcomingMovieBinding
import com.teamfilmo.filmo.domain.model.movie.MovieInfo
import timber.log.Timber

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

            Timber.d("무비 이미지 경로 : ${item.movieImage}")
            Glide
                .with(binding.root.context)
                .asBitmap()
                .load(image)
                .into(binding.movieImage)

            binding.movieCardView.setOnClickListener {
                itemClick?.onClick(adapterPosition)
            }
            binding.movieTitleTxt.text = title
//            binding.ageTxt.text = age.toString()

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

object GenreMapper {
    private val genreMap =
        mapOf(
            28 to "액션",
            12 to "모험",
            16 to "애니메이션",
            35 to "코미디",
            80 to "범죄",
            99 to "다큐멘터리",
            18 to "드라마",
            10751 to "가족",
            14 to "판타지",
            36 to "역사",
            27 to "공포",
            10402 to "음악",
            9648 to "미스터리",
            10749 to "로맨스",
            878 to "SF",
            10770 to "TV 영화",
            53 to "스릴러",
            10752 to "전쟁",
            37 to "서부",
        )

    fun getGenreName(id: Int): String =
        genreMap[id] ?: "알 수 없음"
}
