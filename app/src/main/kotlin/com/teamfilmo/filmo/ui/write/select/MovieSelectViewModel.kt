package com.teamfilmo.filmo.ui.write.select

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.domain.movie.GetTotalPageMovieListUseCase
import com.teamfilmo.filmo.domain.movie.SearchMovieListUseCase
import com.teamfilmo.filmo.ui.write.select.paging.MovieContentResultWithIndex
import com.teamfilmo.filmo.ui.write.select.paging.MoviePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MovieSelectViewModel
    @Inject
    constructor(
        private val searchMovieListUseCase: SearchMovieListUseCase,
        private val getTotalPageMovieListUseCase: GetTotalPageMovieListUseCase,
    ) : BaseViewModel<MovieSelectEffect, MovieSelectEvent>() {
        private val _moviePosterList = MutableStateFlow<MutableList<MovieContentResultWithIndex>>(mutableListOf())
        val moviePosterList: StateFlow<MutableList<MovieContentResultWithIndex>> = _moviePosterList
        private val _moviePosterPathFlow = MutableStateFlow<PagingData<MovieContentResultWithIndex>>(PagingData.empty())
        val moviePosterPathFlow: StateFlow<PagingData<MovieContentResultWithIndex>> = _moviePosterPathFlow

        private fun searchMovies(query: String) {
            viewModelScope.launch {
                getMoviePagingData(query).collectLatest { pagingData ->
                    _moviePosterPathFlow.value = pagingData
                }
            }
        }

    /*
    데이터 스트림과 반응형 스트림이란?
    1. 데이터 스트림 : 사용자가 목록을 스크롤할 때 지속적으로 방출되는 페이지가 매겨진 페이지(PagingData)의 흐름
    - 이는 Flow를 사용하여 처리된다.
    - 비동기 데이터 스트림을 콜드 및 스트림이 적극적으로 수집될 대만 데이터를 가져오거나 처리하는 방식을 사용
    - Pager에 의해 데이터 스트림이 설정되고 Flow<PagingData<MovieContentResultWithIndex>>로 반환된다.

    2. 반응형 스트림 : 데이터 스트림이 사용자 작업(스크롤) 또는 데이터 업데이트와 같은
    변경 사항에 반응하는 방식
    Flow : 반응형 스트림 , 사용자가 UI와 상호작용할 때 새 데이터를 가져오고 내보낸다.

    PagingSource로부터 페이징된 데이터 스트림을 설정.
    Pager 클래스는 PagingSource에서 PagingData 개체의 반응형 스트림을 가져오는 메서드를 제공한다.
    반응형 스트림을 설정하기 위해
    Pager 인스턴스를 만들 때
     1. PagingConfig 개체와
     2. PagingSource 구현의 인스턴스를 가져오는 방법을 Pager에 알려주는 함수를
     인스턴스에 제공해야한다.
     */
        private fun getMoviePagingData(query: String): Flow<PagingData<MovieContentResultWithIndex>> {
            return Pager(
                config =
                    PagingConfig(
                        // 어떻게 데이터를 가져올 것인지
                        // 페이지마다 보여줄 아이템의 수 (동일하지 않아도 됨)
                        pageSize = 20,
                        enablePlaceholders = false,
                    ),
                // PagingSource 구현의 인스턴스를 가져오는 방법을 알려주는 함수
                pagingSourceFactory = {
                    MoviePagingSource(searchMovieListUseCase, query)
                },
                // cachedIn : 데이터 스트림을 공유 가능하게 만들고 제공된 코루틴스코프를 사용하여
                // 로드된 데이터를 캐시한다.
            ).flow.cachedIn(viewModelScope)

            // Pager 객체는 PagingSource 객체에서 load 메서드를 호춣여 LoadParams 객체를
            // 제공하고 LoadResult 객체를 반환받는다.
        }

        override fun handleEvent(event: MovieSelectEvent) {
            when (event) {
                is MovieSelectEvent.SearchMovie -> {
                    event.query?.let { searchMovies(it) }
                }
                is MovieSelectEvent.InitializeMovieList -> {
                    _moviePosterPathFlow.value = PagingData.empty()
                }
                else -> {}
            }
        }
    }
