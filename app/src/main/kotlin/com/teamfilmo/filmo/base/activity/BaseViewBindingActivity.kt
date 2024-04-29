package com.teamfilmo.filmo.base.activity

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.teamfilmo.filmo.base.effect.BaseEffect
import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.base.util.repeatOnStarted
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel

abstract class BaseViewBindingActivity<B : ViewBinding, VM : BaseViewModel<EF, EV>, EF : BaseEffect, EV : BaseEvent>(
    bindingInflater: (layoutInflater: LayoutInflater) -> B,
) : BaseActivity<B>(bindingInflater) {
    protected abstract val viewModel: VM

    override fun initObserver() {
        repeatOnStarted {
            viewModel.effect.collect {
                initEffectObserver(it)
            }
        }
    }

    protected open fun initEffectObserver(effect: EF) = Unit
}
