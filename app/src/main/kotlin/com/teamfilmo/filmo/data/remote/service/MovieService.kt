package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.data.remote.model.movie.MovieResponse
import com.teamfilmo.filmo.data.remote.model.movie.PosterResponse
import com.teamfilmo.filmo.data.remote.model.movie.ThumbnailRequest
import com.teamfilmo.filmo.data.remote.model.movie.detail.MovieDetailRequest
import com.teamfilmo.filmo.data.remote.model.movie.detail.response.DetailMovieResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MovieService {
    /**
     * 영화 검색 리스트
     */
    @POST("/movie/search/movieList")
    suspend fun searchList(
        /**
         * 검색어
         */
        @Body query: MovieRequest?,
    ): Result<MovieResponse>

    /**
     * 영화 상세 정보 검색
     */
    @POST("/movie/search/movieDetail")
    suspend fun searchDetail(
        /**
         * 영화 아이디
         */
        @Body movieId: MovieDetailRequest,
    ): Result<DetailMovieResponse>

    /**
     * 영화 관련 동영상 조회
     */
    @GET("/movie/getVideo")
    suspend fun getVideo(
        /**
         * 영화 아이디
         */
        @Query("movieId")
        movieId: Int,
    ): Result<String>

    /**
     * 영화 이미지 조회
     */
    @POST("/movie/search/posters")
    suspend fun getPoster(
        /**
         * 영화 아이디
         */
        @Body movieId: ThumbnailRequest,
    ): Result<PosterResponse>
}
