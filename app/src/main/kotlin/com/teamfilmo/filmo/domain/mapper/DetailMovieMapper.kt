package com.teamfilmo.filmo.domain.mapper

import com.teamfilmo.filmo.data.remote.entity.movie.detail.response.DetailMovieEntity
import com.teamfilmo.filmo.domain.model.movie.DetailMovie

object DetailMovieMapper {
    fun mapperToDetailMovie(detailMovieEntity: DetailMovieEntity?): DetailMovie =
        DetailMovie(
            title = detailMovieEntity?.title,
            runtime = detailMovieEntity?.runtime,
            overview = detailMovieEntity?.overview,
            certification = detailMovieEntity?.certification,
            originalTitle = detailMovieEntity?.original_title,
            releaseDate = detailMovieEntity?.release_date,
            productionCompanies = detailMovieEntity?.production_companies,
            providers = detailMovieEntity?.providers,
            genres = detailMovieEntity?.genres,
            posterPath = detailMovieEntity?.poster_path,
        )
}
