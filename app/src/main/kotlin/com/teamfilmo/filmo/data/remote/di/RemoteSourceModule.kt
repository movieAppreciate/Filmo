package com.teamfilmo.filmo.data.remote.di

import com.teamfilmo.filmo.data.remote.source.AuthRemoteDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.BookmarkDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.LikeDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.MovieDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.ReportDataSourceImpl
import com.teamfilmo.filmo.data.source.AuthRemoteDataSource
import com.teamfilmo.filmo.data.source.BookmarkDataSource
import com.teamfilmo.filmo.data.source.LikeDataSource
import com.teamfilmo.filmo.data.source.MovieDataSource
import com.teamfilmo.filmo.data.source.ReportDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteSourceModule {
    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl,
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindReportRemoteDataSource(
        reportDataSourceImpl: ReportDataSourceImpl,
    ): ReportDataSource

    @Binds
    @Singleton
    abstract fun bindMovieDataSource(
        movieDataSource: MovieDataSourceImpl,
    ): MovieDataSource

    @Binds
    @Singleton
    abstract fun bindLikeDataSource(
        likeDataSource: LikeDataSourceImpl,
    ): LikeDataSource

    @Binds
    @Singleton
    abstract fun bindBookmarkDatasource(
        bookmarkDataSource: BookmarkDataSourceImpl,
    ): BookmarkDataSource
}
