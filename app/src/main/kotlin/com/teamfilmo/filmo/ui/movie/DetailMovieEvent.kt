package com.teamfilmo.filmo.ui.movie

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class DetailMovieEvent : BaseEvent() {
    data class ClickMovie(
        val movieId: Int,
    ) : DetailMovieEvent()
}
