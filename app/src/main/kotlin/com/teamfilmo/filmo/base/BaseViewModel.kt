package com.teamfilmo.filmo.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    protected inline val TAG: String
        get() = this::class.java.simpleName

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        onError: (throwable: Throwable) -> Unit = ::onError,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        val handler = CoroutineExceptionHandler { _, throwable -> onError(throwable) }
        return viewModelScope.launch(
            context = context + handler,
            start = start,
            block = block,
        )
    }

    protected open fun onError(throwable: Throwable) {
        Timber.tag(TAG).e(throwable)
    }
}
