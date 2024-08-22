package com.teamfilmo.filmo.ui.write.thumbnail

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class ReportThumbnailEvent : BaseEvent() {
    data class SelectPoster(val movieId: String) : ReportThumbnailEvent()

    data class SelectBackground(val movieId: String) : ReportThumbnailEvent()
}
