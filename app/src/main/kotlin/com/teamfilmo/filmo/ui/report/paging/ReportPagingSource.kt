package com.teamfilmo.filmo.ui.report.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamfilmo.filmo.data.remote.model.movie.detail.DetailMovieRequest
import com.teamfilmo.filmo.data.remote.model.report.all.ReportItem
import com.teamfilmo.filmo.domain.bookmark.GetBookmarkLIstUseCase
import com.teamfilmo.filmo.domain.like.CheckLikeStateUseCase
import com.teamfilmo.filmo.domain.movie.detail.GetMovieNameUseCase
import com.teamfilmo.filmo.domain.report.GetReportListUseCase
import com.teamfilmo.filmo.domain.report.GetReportUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class ReportPagingSource(
    private val getReportUseCase: GetReportUseCase,
    private val getMovieNameUseCase: GetMovieNameUseCase,
    private val getReportListUseCase: GetReportListUseCase,
    private val getBookmarkListUseCase: GetBookmarkLIstUseCase,
    private val checkLikeStateUseCase: CheckLikeStateUseCase,
) : PagingSource<String, ReportItem>() {
    override fun getRefreshKey(state: PagingState<String, ReportItem>): String? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.reportId
        }

    private suspend fun getName(reportId: String): String =
        withContext(Dispatchers.IO) {
            val report = getReportUseCase(reportId).firstOrNull()
            report?.let {
                getMovieNameUseCase(DetailMovieRequest(it.movieId.toString())).firstOrNull() ?: ""
            } ?: ""
        }

    /*
   PagingSource의 생성자에 제공된 매개변스를  load 메서드에 넘겨주어  적합한 데이터를 로드한다.

     */
    override suspend fun load(params: LoadParams<String>): LoadResult<String, ReportItem> =
        try {
            val lastReportId = if (params is LoadParams.Refresh) null else params.key

            val combinedData =
                combine(
                    getReportListUseCase(lastReportId),
                    getBookmarkListUseCase(),
                ) { reportListResponse, bookmarkList ->
                    val reportList = reportListResponse?.searchReport ?: emptyList()
                    val hasNext = reportListResponse?.hasNext ?: false

                    reportList.map { reportItem ->
                        ReportItem(
                            reportId = reportItem.reportId,
                            title = reportItem.title,
                            content = reportItem.content,
                            createDate = reportItem.createDate,
                            imageUrl = reportItem.imageUrl,
                            nickname = reportItem.nickname,
                            likeCount = reportItem.likeCount,
                            replyCount = reportItem.replyCount,
                            bookmarkCount = reportItem.bookmarkCount,
                            isBookmark = bookmarkList.any { it.reportId == reportItem.reportId },
                            isLiked = checkLikeStateUseCase(reportItem.reportId, "report").first()!!.isLike,
                            likeId = checkLikeStateUseCase(reportItem.reportId, "report").first()?.likeId,
                            movieName = getName(reportItem.reportId),
                        )
                    } to hasNext
                }.first()

            val (items, hasNext) = combinedData
            val nextKey =
                if (!hasNext) {
                    null
                } else {
                    items.lastOrNull()?.reportId
                }

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            //  네트워크 에러와 같은 내용을 처리하고 LoadResult.Error을 반환한다.
            e.printStackTrace()
            LoadResult.Error(e)
        }
}
