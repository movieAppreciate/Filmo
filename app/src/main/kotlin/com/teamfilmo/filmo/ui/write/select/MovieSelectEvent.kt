package com.teamfilmo.filmo.ui.write.select

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class MovieSelectEvent : BaseEvent() {
    data class SearchMovie(val query: String?) : MovieSelectEvent()

    data object InitializeMovieList : MovieSelectEvent()
}
