package com.teamfilmo.filmo.data.remote.service.di

import com.teamfilmo.filmo.data.remote.service.AuthService
import com.teamfilmo.filmo.data.remote.service.BookmarkService
import com.teamfilmo.filmo.data.remote.service.ComplaintService
import com.teamfilmo.filmo.data.remote.service.FollowService
import com.teamfilmo.filmo.data.remote.service.InquiryService
import com.teamfilmo.filmo.data.remote.service.LikeService
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
        retrofit: Retrofit,
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookmarkService(
        retrofit: Retrofit,
    ): BookmarkService {
        return retrofit.create(BookmarkService::class.java)
    }

    @Provides
    @Singleton
    fun provideComplaintService(
        retrofit: Retrofit,
    ): ComplaintService {
        return retrofit.create(ComplaintService::class.java)
    }

    @Provides
    @Singleton
    fun provideFollowService(
        retrofit: Retrofit,
    ): FollowService {
        return retrofit.create(FollowService::class.java)
    }

    @Provides
    @Singleton
    fun provideInquiryService(
        retrofit: Retrofit,
    ): InquiryService {
        return retrofit.create(InquiryService::class.java)
    }

    @Provides
    @Singleton
    fun provideLikeService(
        retrofit: Retrofit,
    ): LikeService {
        return retrofit.create(LikeService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieService(
        retrofit: Retrofit,
    ): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationService(
        retrofit: Retrofit,
    ): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }

    @Provides
    @Singleton
    fun provideReplyService(
        retrofit: Retrofit,
    ): ReplyService {
        return retrofit.create(ReplyService::class.java)
    }

    @Provides
    @Singleton
    fun provideReportService(
        retrofit: Retrofit,
    ): ReportService {
        return retrofit.create(ReportService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(
        retrofit: Retrofit,
    ): UserService {
        return retrofit.create(UserService::class.java)
    }
}
