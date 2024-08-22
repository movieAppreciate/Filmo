package com.teamfilmo.filmo.ui.write.movie

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.domain.movie.GetTotalPageMovieListUseCase
import com.teamfilmo.filmo.domain.movie.SearchMovieListUseCase
import com.teamfilmo.filmo.model.movie.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MovieSelectViewModel
    @Inject
    constructor(
        private val searchMovieListUseCase: SearchMovieListUseCase,
        private val getTotalPageMovieListUseCase: GetTotalPageMovieListUseCase,
    ) :
    BaseViewModel<MovieSelectEffect, MovieSelectEvent>() {
        private var previousQuery: String? = null
        private var myQuery: MovieRequest? = null
        private val _movieList = MutableStateFlow<List<Result>>(emptyList())
        private var _totalPage: MutableStateFlow<Int> = MutableStateFlow(1)

        val totalPage: StateFlow<Int> =
            _totalPage.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = 1,
            )

        val movieList: StateFlow<List<Result>> =
            _movieList.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = arrayListOf(),
            )

        private val _moviePosterUriList = MutableStateFlow<List<String>>(emptyList())
        val moviePosterUriList: StateFlow<List<String>> =
            _moviePosterUriList.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )

        override fun handleEvent(event: MovieSelectEvent) {
            when (event) {
                is MovieSelectEvent.SearchMovie -> {
                    searchMovieList(query = event.query)
                }

                is MovieSelectEvent.InitializeMovieList -> {
                    _movieList.value = emptyList()
                    _moviePosterUriList.value = emptyList()
                }
            }
        }

        private fun getTotalMoviePage(query: MovieRequest?) {
            viewModelScope.launch {
                getTotalPageMovieListUseCase(query).collect {
                    _totalPage.value = it
                }
            }
        }

        private fun searchMovieList(
            query: String?,
        ) {
            viewModelScope.launch {
                if (previousQuery != query) {
                    previousQuery = query

                    myQuery = query?.let { MovieRequest(it) }

                    // 비동기 작업 전 상태를 비워줍니다.
                    _movieList.update { emptyList() }
                    _moviePosterUriList.update { emptyList() }


                    searchMovieListUseCase(myQuery).collect {
                        getTotalMoviePage(myQuery)
                        _movieList.update {
                            it.distinctBy { it.id }
                        }

                        val imageBaseUrl = "https://image.tmdb.org/t/p/original"

                        val updatedResultList =
                            it.distinctBy { it.id }.mapNotNull {
                                it.posterPath?.let { posterPath ->
                                    imageBaseUrl + posterPath
                                }
                            }

                        _moviePosterUriList.update { updatedResultList }
                        sendEffect(MovieSelectEffect.SearchMovie)
                    }
                }
            }
        }
    }
