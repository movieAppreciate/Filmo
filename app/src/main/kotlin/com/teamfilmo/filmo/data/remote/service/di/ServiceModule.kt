package com.teamfilmo.filmo.data.remote.service.di

import com.teamfilmo.filmo.data.remote.network.policy.MainApiRetrofit
import com.teamfilmo.filmo.data.remote.network.policy.MovieApiRetrofit
import com.teamfilmo.filmo.data.remote.service.AuthService
import com.teamfilmo.filmo.data.remote.service.BookmarkService
import com.teamfilmo.filmo.data.remote.service.ComplaintService
import com.teamfilmo.filmo.data.remote.service.FollowService
import com.teamfilmo.filmo.data.remote.service.InquiryService
import com.teamfilmo.filmo.data.remote.service.LikeService
import com.teamfilmo.filmo.data.remote.service.MovieApiService
import com.teamfilmo.filmo.data.remote.service.MovieService
import com.teamfilmo.filmo.data.remote.service.NotificationService
import com.teamfilmo.filmo.data.remote.service.ReplyService
import com.teamfilmo.filmo.data.remote.service.ReportService
import com.teamfilmo.filmo.data.remote.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideAuthService(
        @MainApiRetrofit retrofit: Retrofit,
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookmarkService(
        @MainApiRetrofit retrofit: Retrofit,
    ): BookmarkService {
        return retrofit.create(BookmarkService::class.java)
    }

    @Provides
    @Singleton
    fun provideComplaintService(
        @MainApiRetrofit retrofit: Retrofit,
    ): ComplaintService {
        return retrofit.create(ComplaintService::class.java)
    }

    @Provides
    @Singleton
    fun provideFollowService(
        @MainApiRetrofit retrofit: Retrofit,
    ): FollowService {
        return retrofit.create(FollowService::class.java)
    }

    @Provides
    @Singleton
    fun provideInquiryService(
        @MainApiRetrofit retrofit: Retrofit,
    ): InquiryService {
        return retrofit.create(InquiryService::class.java)
    }

    @Provides
    @Singleton
    fun provideLikeService(
        @MainApiRetrofit retrofit: Retrofit,
    ): LikeService {
        return retrofit.create(LikeService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieService(
        @MainApiRetrofit retrofit: Retrofit,
    ): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationService(
        @MainApiRetrofit retrofit: Retrofit,
    ): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }

    @Provides
    @Singleton
    fun provideReplyService(
        @MainApiRetrofit retrofit: Retrofit,
    ): ReplyService {
        return retrofit.create(ReplyService::class.java)
    }

    @Provides
    @Singleton
    fun provideReportService(
        @MainApiRetrofit retrofit: Retrofit,
    ): ReportService {
        return retrofit.create(ReportService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(
        @MainApiRetrofit retrofit: Retrofit,
    ): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieApiService(
        @MovieApiRetrofit retrofit: Retrofit,
    ): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }
}
