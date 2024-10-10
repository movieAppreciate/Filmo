package com.teamfilmo.filmo.ui.write.thumbnail

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.domain.movie.GetMovieBackDropListUseCase
import com.teamfilmo.filmo.domain.movie.GetMoviePosterListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ReportThumbnailViewModel
    @Inject
    constructor(
        private val getMoviePosterListUseCase: GetMoviePosterListUseCase,
        private val getMovieBackDropListUseCase: GetMovieBackDropListUseCase,
    ) : BaseViewModel<ReportThumbnailEffect, ReportThumbnailEvent>() {
        private var _moviePosterList = MutableStateFlow<List<String>>(emptyList())
        private var _movieBackdropList = MutableStateFlow<List<String>>(emptyList())

        val movieBackdropList: StateFlow<List<String>> =
            _movieBackdropList.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )
        val moviePosterList: StateFlow<List<String>> =
            _moviePosterList.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )

        override fun handleEvent(event: ReportThumbnailEvent) {
            when (event) {
                is ReportThumbnailEvent.SelectPoster -> {
                    getPoster(event.movieId)
                }
                is ReportThumbnailEvent.SelectBackground -> {
                    getBackDrop(event.movieId)
                }
                else -> {}
            }
        }

        private fun getPoster(movieId: String) {
            viewModelScope.launch {
                getMoviePosterListUseCase(movieId).collect {
                    _moviePosterList.value = it
                }
            }
        }

        private fun getBackDrop(movieId: String) {
            viewModelScope.launch {
                getMovieBackDropListUseCase(movieId).collect {
                    _movieBackdropList.value = it
                }
            }
        }
    }
