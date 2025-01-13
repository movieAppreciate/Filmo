package com.teamfilmo.filmo.data.remote.entity.bookmark

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkCountResponse(
    @SerializedName("bookmarkCount")
    val count: Int,
)
