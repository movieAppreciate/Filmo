package com.teamfilmo.filmo.ui.search

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor() : BaseViewModel<SearchEffect, SearchEvent>()
