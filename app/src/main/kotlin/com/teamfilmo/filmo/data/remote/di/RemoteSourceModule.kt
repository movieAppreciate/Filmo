package com.teamfilmo.filmo.data.remote.di

import com.teamfilmo.filmo.data.remote.source.AuthRemoteDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.BookmarkDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.ComplaintDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.FollowDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.LikeDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.MovieApiDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.MovieDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.ReplyDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.ReportDataSourceImpl
import com.teamfilmo.filmo.data.remote.source.UserDataSourceImpl
import com.teamfilmo.filmo.data.source.AuthRemoteDataSource
import com.teamfilmo.filmo.data.source.BookmarkDataSource
import com.teamfilmo.filmo.data.source.ComplaintDataSource
import com.teamfilmo.filmo.data.source.FollowDataSource
import com.teamfilmo.filmo.data.source.LikeDataSource
import com.teamfilmo.filmo.data.source.MovieApiDataSource
import com.teamfilmo.filmo.data.source.MovieDataSource
import com.teamfilmo.filmo.data.source.ReplyDataSource
import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.data.source.UserDataSource
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

    @Binds
    @Singleton
    abstract fun bindMovieApiDataSource(
        movieApiDataSource: MovieApiDataSourceImpl,
    ): MovieApiDataSource

    @Binds
    @Singleton
    abstract fun bindFollowDataSource(
        followDataSource: FollowDataSourceImpl,
    ): FollowDataSource

    @Binds
    @Singleton
    abstract fun bindReplyDataSource(
        replyDataSource: ReplyDataSourceImpl,
    ): ReplyDataSource

    @Binds
    @Singleton
    abstract fun bindUserDataSource(
        userDataSource: UserDataSourceImpl,
    ): UserDataSource

    @Binds
    @Singleton
    abstract fun bindComplaintDataSource(
        complaintDataSource: ComplaintDataSourceImpl,
    ): ComplaintDataSource
}
