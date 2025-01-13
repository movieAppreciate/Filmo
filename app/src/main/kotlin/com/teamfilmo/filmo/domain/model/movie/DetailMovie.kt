package com.teamfilmo.filmo.domain.model.movie

import com.teamfilmo.filmo.data.remote.entity.movie.detail.response.Genre
import com.teamfilmo.filmo.data.remote.entity.movie.detail.response.ProductionCompany
import com.teamfilmo.filmo.data.remote.entity.movie.detail.response.Providers

data class DetailMovie(
    val title: String? = null,
    val runtime: Int? = null,
    val overview: String? = null,
    val certification: String? = null,
    val originalTitle: String? = null,
    val releaseDate: String? = null,
    val productionCompanies: List<ProductionCompany>? = emptyList(),
    val providers: Providers? = null,
    val genres: List<Genre>? = emptyList(),
    val posterPath: String? = null,
)
