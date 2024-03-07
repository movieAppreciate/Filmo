package com.teamfilmo.filmo.data.remote.network.adapter

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit

class ResultCallAdapter(
    private val type: Type,
    private val maxRetryCount: Int,
) : CallAdapter<Type, Call<Result<Type>>> {
    override fun responseType(): Type = type

    override fun adapt(call: Call<Type>): Call<Result<Type>> = ResultCall(call, maxRetryCount)

    class Factory(
        private val maxRetryCount: Int,
    ) : CallAdapter.Factory() {
        override fun get(
            returnType: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit,
        ): CallAdapter<*, *>? {
            val returnTypeClass = getRawType(returnType)
            if (returnTypeClass != Call::class.java) {
                return null
            }

            val callType =
                getParameterUpperBound(
                    0,
                    returnType as ParameterizedType,
                )

            val rawType = getRawType(callType)
            if (rawType != Result::class.java) {
                return null
            }

            val resultType =
                getParameterUpperBound(
                    0,
                    callType as ParameterizedType,
                )
            return ResultCallAdapter(
                resultType,
                maxRetryCount,
            )
        }
    }
}
