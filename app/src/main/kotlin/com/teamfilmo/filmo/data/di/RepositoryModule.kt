package com.teamfilmo.filmo.data.di

import com.teamfilmo.filmo.data.remote.source.ReportRepositoryImpl
import com.teamfilmo.filmo.data.repository.AuthRepositoryImpl
import com.teamfilmo.filmo.data.repository.BookmarkRepositoryImpl
import com.teamfilmo.filmo.data.repository.LikeRepositoryImpl
import com.teamfilmo.filmo.data.repository.MovieRepositoryImpl
import com.teamfilmo.filmo.domain.repository.AuthRepository
import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import com.teamfilmo.filmo.domain.repository.LikeRepository
import com.teamfilmo.filmo.domain.repository.MovieRepository
import com.teamfilmo.filmo.domain.repository.ReportRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl,
    ): ReportRepository

    @Binds
    abstract fun bindMovieRepository(
        movieRepository: MovieRepositoryImpl,
    ): MovieRepository

    @Binds
    abstract fun bindBookmarkRepository(
        bookmarkRepository: BookmarkRepositoryImpl,
    ): BookmarkRepository

    @Binds
    abstract fun bindLikeRepository(
        likeRepository: LikeRepositoryImpl,
    ): LikeRepository
}
