package com.teamfilmo.filmo.ui.write.select

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.data.remote.model.movie.Result
import com.teamfilmo.filmo.domain.movie.GetTotalPageMovieListUseCase
import com.teamfilmo.filmo.domain.movie.SearchMovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
        private var totalPage = 1
        private var myQuery: MovieRequest? = null
        private val _movieList = MutableStateFlow<List<Result>>(emptyList())

        val movieList: StateFlow<List<Result>> =
            _movieList.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
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
                    searchMovieList(query = event.query, event.page)
                }

                is MovieSelectEvent.InitializeMovieList -> {
                    _movieList.value = emptyList()
                    _moviePosterUriList.value = arrayListOf()
                }
                is MovieSelectEvent.LoadNextPageMovie -> {
                    loadNextMoviePage(event.page)
                }
                else -> {}
            }
        }

        private fun searchMovieList(
            query: String?,
            page: Int,
        ) {
            viewModelScope.launch {
                myQuery =
                    query?.let {
                        MovieRequest(query = it, page = page)
                    }
                getTotalPageMovieListUseCase(myQuery).collect {
                    totalPage = it
                    if (page > it) {
                        return@collect
                    }
                    if (page == it) {
                        sendEffect(MovieSelectEffect.NotifyLastPage)
                    }
                }
                searchMovieListUseCase(myQuery).collect { resultList ->
                    if (page == 1) {
                        _movieList.value =
                            resultList.distinctBy {
                                it.id
                            }
                    } else {
                        _movieList.update { oldList ->
                            oldList + resultList.distinctBy { it.id }
                        }
                    }

                    val imageBaseUrl = "https://image.tmdb.org/t/p/original"

                    val updatedResultList =
                        resultList.mapNotNull {
                            it.posterPath?.let { posterPath ->
                                imageBaseUrl + posterPath
                            }
                        }
                    if (page == 1) {
                        _moviePosterUriList.value = updatedResultList
                    } else {
                        _moviePosterUriList.update { oldList ->
                            oldList + updatedResultList
                        }
                    }
                    sendEffect(MovieSelectEffect.SearchMovie)
                }
            }
        }

        private fun loadNextMoviePage(currentPage: Int) {
            Timber.d("2. 뷰모델의 loadNextMoviePage 호출 ")
            if (currentPage > totalPage) {
                return
            }

            viewModelScope.launch {
                val searchResultDeferred =
                    async {
                        searchMovieList(myQuery?.query, currentPage)
                    }
                searchResultDeferred.await()
                sendEffect(MovieSelectEffect.LoadNextPage)
            }
        }
    }
