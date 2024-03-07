package com.teamfilmo.filmo.data.remote.network.adapter

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultCall<T : Any>(
    private val call: Call<T>,
    private val maxRetryCount: Int,
    private val retryCount: Int = 0,
) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        call.enqueue(
            object : Callback<T> {
                override fun onResponse(
                    call: Call<T>,
                    response: Response<T>,
                ) {
                    val networkCall =
                        when {
                            response.isSuccessful && response.body() != null -> {
                                Result.success(response.body()!!)
                            }

                            else -> {
                                Result.failure(Throwable(response.message()))
                            }
                        }

                    callback.onResponse(this@ResultCall, Response.success(networkCall))
                }

                override fun onFailure(
                    call: Call<T>,
                    t: Throwable,
                ) {
                    if (retryCount < maxRetryCount) {
                        clone().enqueue(callback)
                    } else {
                        val result = Result.failure<T>(t)
                        callback.onResponse(this@ResultCall, Response.success(result))
                    }
                }
            },
        )
    }

    override fun clone(): Call<Result<T>> = ResultCall(call, maxRetryCount, retryCount + 1)

    override fun execute(): Response<Result<T>> {
        throw UnsupportedOperationException("ResponseCall doesn't support execute")
    }

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() {
        call.cancel()
    }

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()
}
