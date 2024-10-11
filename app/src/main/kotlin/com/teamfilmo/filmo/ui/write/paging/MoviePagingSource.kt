package com.teamfilmo.filmo.ui.write.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.data.remote.model.movie.Result
import com.teamfilmo.filmo.domain.movie.SearchMovieListUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class MoviePagingSource(
    private val searchMovieListUseCase: SearchMovieListUseCase,
    private val query: String?,
) : PagingSource<Int, Result>() {
    // Int : 페이지 번호
    // Result : 영화 정보를 가지고 있는 데이터 클래스

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val page = params.key ?: 1

        return try {
            if (page > 1) {
                delay(3000)
            }
            val movieRequest = query?.let { MovieRequest(it, page) }
            val response = searchMovieListUseCase(movieRequest)

            LoadResult.Page(
                data = response.first(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.first().isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
