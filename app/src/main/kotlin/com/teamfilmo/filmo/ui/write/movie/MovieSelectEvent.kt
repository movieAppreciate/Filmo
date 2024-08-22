package com.teamfilmo.filmo.ui.write.movie

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class MovieSelectEvent : BaseEvent() {
    data class SearchMovie(val query: String?) : MovieSelectEvent()

    data object InitializeMovieList : MovieSelectEvent()
//    data class LoadNextPageMovie(val query: String?, val page: Int) : MovieSelectEvent()
}
