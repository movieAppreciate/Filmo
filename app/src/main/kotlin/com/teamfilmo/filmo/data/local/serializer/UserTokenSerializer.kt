package com.teamfilmo.filmo.data.local.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.teamfilmo.filmo.data.local.model.UserToken
import java.io.InputStream
import java.io.OutputStream

object UserTokenSerializer : Serializer<UserToken> {
    private val TAG = this::class.java.simpleName

    override val defaultValue: UserToken
        get() = UserToken.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserToken {
        try {
            return UserToken.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(
        t: UserToken,
        output: OutputStream,
    ) {
        t.writeTo(output)
    }
}
