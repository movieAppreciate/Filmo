package com.teamfilmo.filmo.ui.write.select.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamfilmo.filmo.data.remote.model.movie.MovieContentResult
import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.domain.movie.SearchMovieListUseCase
import kotlinx.coroutines.flow.first

data class MovieContentResultWithIndex(
    val index: Int,
    val result: MovieContentResult,
)

class MoviePagingSource(
    private val searchMovieListUseCase: SearchMovieListUseCase,
    private val query: String?,
) : PagingSource<Int, MovieContentResultWithIndex>() {
    // key :  Int : 페이지 번호
    // value : Result : 영화 정보를 가지고 있는 데이터 클래스

    /*
    getRefreshKey : 사용자가 목록을 새로 고치거나 구성을 변경하는 경우, 목록에서 멀리 스크롤하는 경우
    PagingSource가 다시 로드해야하는 앵커 위치 또는 키를 결정하는 함수

    키는 목록의 현재 위치 주변에 데이터를 로드하는 참조점 역할을 한다.

    anchorPage: 현재 목록에 표시된 페이지
    anchorPage?.prevKey?.plus(1) : 현재 페이지에  prevKey가 있는 경우, 이전 페이지 뒤에 나오는 페이지부터 새로고림하라는 듯
    현재 페이지를 얻으려면 prevKey를 1씩 증가

    anchorPage?.nextKey?.minus(1) : prevKey가 null 인 경우 nextKey가 존재하는지 확인한다.

     */
    override fun getRefreshKey(state: PagingState<Int, MovieContentResultWithIndex>): Int? =
        state.anchorPosition?.let {
            // anchorPosition 현재 아이템에 표시된 아이템 중
            // 가장 처음 위치한 아이템의 인덱스
            val anchorPage = state.closestPageToPosition(it)
            // closestPageToPosition는 주어진 위치에서 가장 가까운 로드된 페이지를 찾음
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            /* 이전 페이지가 잇다면 이전 페이지에서 + 1
             이전 페이지가 없고 다음페이지가 있다면 다음 페이지 -1 을 하면
             현재 페이지인 anchorPage를 구할 수 있다.
             */
        }

    /*
   PagingSource의 생성자에 제공된 매개변스를  load 메서드에 넘겨주어  적합한 데이터를 로드한다.
*/
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieContentResultWithIndex> {
        val start = params.key ?: 1

        return try {
            val movieRequest = query?.let { MovieRequest(it, start) }
            val response = searchMovieListUseCase(movieRequest)

            val movieContentResultWithIndices =
                response.first().mapIndexed { index, result ->
                    MovieContentResultWithIndex((start - 1) * params.loadSize + index, result)
                }

            /*
             LoadResult : 로드 작업의 결과를 포함
              로드에 성공했을 경우 LoadResult.Page를 반환
             로드 실패 시 LoadResult.Error를 반환한다.
             */

            LoadResult.Page(
                data = movieContentResultWithIndices,
                /*
                1. 페이지 번호를 기준으로 항목 목록의 페이지를 매기는 경우,
                preKey : 현재 페이지 번호 -1
                nextKey : 현재 페이지 번호 +1

                2. 아이템 인덱스 기준으로 페이지를 매기는 경우
                페이지 크기  : params.loadsize
                prevKey : 로드할 첫번 째 항목의 인덱스 (초기 : 0)
                    0 ~ 19 까지 첫번째 페이지에 로그되었다면
                    20이 시작 인덱스가 된다.
                nextKey : 현재 항목을 로드한 후 마지막에 로드된 항목의 인덱스
                    즉 시작 인덱스 + 로드된 아이템의 사이즈
                 */
                prevKey = if (start == 1) null else start - 1,
                nextKey = if (response.first().isEmpty()) null else start + 1,
            )
        } catch (e: Exception) {
            //  네트워크 에러와 같은 내용을 처리하고 LoadResult.Error을 반환한다.
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}
