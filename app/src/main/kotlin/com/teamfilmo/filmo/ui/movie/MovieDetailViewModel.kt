package com.teamfilmo.filmo.ui.movie

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.movie.detail.response.DetailMovieResponse
import com.teamfilmo.filmo.domain.movie.detail.SearchMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MovieDetailViewModel
    @Inject
    constructor(
        private val searchMovieDetailUseCase: SearchMovieDetailUseCase,
    ) : BaseViewModel<DetailMovieEffect, DetailMovieEvent>() {
        override fun handleEvent(event: DetailMovieEvent) {
            when (event) {
                is DetailMovieEvent.ClickMovie -> searchMovieDetail(event.movieId)
            }
        }

        /*
  영화 상세 정보
         */
        private val _movieDetailInfo =
            MutableStateFlow(
                DetailMovieResponse(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
            )

        val movieDetailInfo = _movieDetailInfo.asStateFlow()

        fun searchMovieDetail(movieId: Int) {
            viewModelScope.launch {
                searchMovieDetailUseCase(movieId).collect {
                    if (it != null) {
                        _movieDetailInfo.value = it
                        sendEffect(DetailMovieEffect.ShowDetailMovie)
                    }
                }
            }
        }

        fun getMovieOverview(): String? {
            return _movieDetailInfo.value.overview
        }
    }
