package com.teamfilmo.filmo.data.remote.network.interceptor

import com.teamfilmo.filmo.data.remote.service.AuthService
import com.teamfilmo.filmo.data.source.UserTokenSource
import java.net.HttpURLConnection
import javax.inject.Provider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

// 안드로이드 앱의 네트워크 통신에서 사용자 인증을 처리하는 코드
// Interceptor
// - 모든 네트워크 요청을 가로채서 인증토큰을 관리하는 역할
// - JWT 기반의 인증 시스템에서 매우 중요한 컴포넌트
class UserAuthInterceptor(
    // Provider : 필요할 때만 의존성을 가져오는 지연 초기화 패턴
    private val dataStore: Provider<UserTokenSource>,
    private val authService: Provider<AuthService>,
) : Interceptor {
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response =
        runBlocking {
            val request = chain.request()

            // 인증이 필요없는 요청의 경우 : No AUTH 헤더를 확인하여 token을 넣지 않는다.
            if ("true" in request.headers.values("NO-AUTH")) {
                return@runBlocking chain.proceed(request)
            }

            val accessToken =
                dataStore
                    .get()
                    .getUserToken()
                    .firstOrNull()
                    ?.let { it.ifEmpty { null } }

            Timber.d("로그인 access token ", accessToken.toString())

            val response = chain.proceedWithToken(request, accessToken)

            // 정상 응답인 경우 그대로 return || accessToken이 없는 경우(Logout, 처음 사용자), 그대로 return
            if (response.code != HttpURLConnection.HTTP_UNAUTHORIZED || accessToken == null) {
                return@runBlocking response
            }

            // 토큰 갱신 프로세스
            val (newAccessToken, newRefreshToken) =
                mutex.withLock {
                    // mutex.withLock : 동시에 여러 토큰 갱신 요청이 발생하는 것을 방지
                    val currentAccessToken = dataStore.get().getUserToken().firstOrNull()
                    val currentRefreshToken = dataStore.get().getRefreshToken().firstOrNull()

                    Timber.d("currentRefreshToken :$currentRefreshToken")

                    if (currentAccessToken.isNullOrBlank() || currentRefreshToken.isNullOrBlank()) {
                        return@withLock Token(null, null)
                    }

                    val result = authService.get().refreshToken(currentAccessToken, currentRefreshToken)

                    return@withLock result
                        .map { Token(it.accessToken, it.refreshToken) }
                        .fold(
                            onSuccess = { it },
                            onFailure = { Token(null, null) },
                        )
                }

            return@runBlocking if (newAccessToken != null && newRefreshToken != null) {
                dataStore.get().updateToken(newAccessToken, newRefreshToken)
                chain.proceedWithToken(request, newAccessToken)
            } else {
                dataStore.get().clearUserToken()
                response
            }
        }

    private fun Interceptor.Chain.proceedWithToken(
        request: Request,
        token: String?,
    ): Response =
        request
            .newBuilder()
            .apply {
                if (token.isNullOrBlank().not()) {
                    addHeader(AUTHORIZATION, "Bearer $token")
                }
            }.build()
            .let(::proceed)

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }

    data class Token(
        val accessToken: String?,
        val refreshToken: String?,
    )
}
