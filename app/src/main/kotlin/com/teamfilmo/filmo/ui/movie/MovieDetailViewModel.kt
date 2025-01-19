package com.teamfilmo.filmo.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.domain.model.movie.DetailMovie
import com.teamfilmo.filmo.domain.movie.detail.SearchMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MovieDetailViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val searchMovieDetailUseCase: SearchMovieDetailUseCase,
    ) : BaseViewModel<DetailMovieEffect, DetailMovieEvent>() {
        companion object {
            private const val KEY_MOVIE_ID = "movieId" // args와 동일한 키 사용!
        }

        val movieId: Int? = savedStateHandle[KEY_MOVIE_ID]

        init {
            searchMovieDetail()
        }

        override fun handleEvent(event: DetailMovieEvent) {
            when (event) {
                is DetailMovieEvent.ClickMovie -> {
                }
            }
        }

        /*
        영화 상세 내용
         */
        private val _movieContent = MutableStateFlow("")
        val movieContent: StateFlow<String> = _movieContent

        /*
  영화 상세 정보
         */
        private val _movieDetailInfo =
            MutableStateFlow(
                DetailMovie(null, null, null, null, null, null, null, null, null, null),
            )

        val movieDetailInfo = _movieDetailInfo.asStateFlow()

        fun searchMovieDetail() {
            viewModelScope.launch {
                if (movieId == null) return@launch
                searchMovieDetailUseCase(movieId).collect {
                    if (it != null) {
                        _movieDetailInfo.value = it
                        _movieContent.value = it.overview.toString()
                        sendEffect(DetailMovieEffect.ShowDetailMovie)
                    }
                }
            }
        }
    }
