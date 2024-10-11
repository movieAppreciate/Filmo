package com.teamfilmo.filmo.ui.write.select

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.movie.Result
import com.teamfilmo.filmo.domain.movie.GetTotalPageMovieListUseCase
import com.teamfilmo.filmo.domain.movie.SearchMovieListUseCase
import com.teamfilmo.filmo.ui.write.paging.MoviePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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
        private val _movieList = MutableStateFlow<MutableList<Result>>(mutableListOf())
        val movieList: StateFlow<MutableList<Result>> = _movieList
        private val _movieFlow = MutableStateFlow<PagingData<Result>>(PagingData.empty())
        val movieFlow: StateFlow<PagingData<Result>> = _movieFlow

        private fun searchMovies(query: String) {
            viewModelScope.launch {
                getMoviePagingData(query).collectLatest { pagingData ->
                    _movieFlow.value = pagingData
                    Timber.d("_movieFlow :${_movieFlow.value}")
                }
            }
        }

        private fun getMoviePagingData(query: String): Flow<PagingData<Result>> {
            return Pager(
                config =
                    PagingConfig(
                        // Todo : 토탈 페이지 넘겨주기
                        pageSize = 20,
                        enablePlaceholders = true,
                    ),
                pagingSourceFactory = {
                    MoviePagingSource(searchMovieListUseCase, query)
                },
            ).flow.cachedIn(viewModelScope)
        }

        override fun handleEvent(event: MovieSelectEvent) {
            when (event) {
                is MovieSelectEvent.SearchMovie -> {
                    event.query?.let { searchMovies(it) }
                }

                is MovieSelectEvent.InitializeMovieList -> {
                    _movieFlow.value = PagingData.empty()
                }
                else -> {}
            }
        }
    }
