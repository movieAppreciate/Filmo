package com.teamfilmo.filmo.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.effect.BaseEffect
import com.teamfilmo.filmo.base.event.BaseEvent
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<EF : BaseEffect, EV : BaseEvent> : ViewModel() {
    protected inline val TAG: String
        get() = this::class.java.simpleName

    private val _effect: MutableSharedFlow<EF> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    protected fun sendEffect(event: EF) {
        viewModelScope.launch {
            _effect.emit(event)
        }
    }

    open fun handleEvent(event: EV) = Unit

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
