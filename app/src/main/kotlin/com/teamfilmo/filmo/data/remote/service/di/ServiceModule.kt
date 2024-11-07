package com.teamfilmo.filmo.data.remote.service.di

import com.teamfilmo.filmo.data.remote.network.policy.MainApiRetrofit
import com.teamfilmo.filmo.data.remote.network.policy.MovieApiRetrofit
import com.teamfilmo.filmo.data.remote.service.AuthService
import com.teamfilmo.filmo.data.remote.service.BlockService
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
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideBookmarkService(
        @MainApiRetrofit retrofit: Retrofit,
    ): BookmarkService = retrofit.create(BookmarkService::class.java)

    @Provides
    @Singleton
    fun provideComplaintService(
        @MainApiRetrofit retrofit: Retrofit,
    ): ComplaintService = retrofit.create(ComplaintService::class.java)

    @Provides
    @Singleton
    fun provideFollowService(
        @MainApiRetrofit retrofit: Retrofit,
    ): FollowService = retrofit.create(FollowService::class.java)

    @Provides
    @Singleton
    fun provideInquiryService(
        @MainApiRetrofit retrofit: Retrofit,
    ): InquiryService = retrofit.create(InquiryService::class.java)

    @Provides
    @Singleton
    fun provideLikeService(
        @MainApiRetrofit retrofit: Retrofit,
    ): LikeService = retrofit.create(LikeService::class.java)

    @Provides
    @Singleton
    fun provideMovieService(
        @MainApiRetrofit retrofit: Retrofit,
    ): MovieService = retrofit.create(MovieService::class.java)

    @Provides
    @Singleton
    fun provideNotificationService(
        @MainApiRetrofit retrofit: Retrofit,
    ): NotificationService = retrofit.create(NotificationService::class.java)

    @Provides
    @Singleton
    fun provideReplyService(
        @MainApiRetrofit retrofit: Retrofit,
    ): ReplyService = retrofit.create(ReplyService::class.java)

    @Provides
    @Singleton
    fun provideReportService(
        @MainApiRetrofit retrofit: Retrofit,
    ): ReportService = retrofit.create(ReportService::class.java)

    @Provides
    @Singleton
    fun provideUserService(
        @MainApiRetrofit retrofit: Retrofit,
    ): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideMovieApiService(
        @MovieApiRetrofit retrofit: Retrofit,
    ): MovieApiService = retrofit.create(MovieApiService::class.java)

    @Provides
    @Singleton
    fun provideBlockService(
        @MainApiRetrofit retrofit: Retrofit,
    ): BlockService = retrofit.create(BlockService::class.java)
}
