package com.teamfilmo.filmo.base.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.teamfilmo.filmo.base.effect.BaseEffect
import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.base.util.repeatOnStarted
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel

abstract class BaseViewBindingFragment<B : ViewBinding, VM : BaseViewModel<EF, EV>, EF : BaseEffect, EV : BaseEvent>(
    bindingInflater: (layoutInflater: LayoutInflater, viewGroup: ViewGroup?, attachToParent: Boolean) -> B,
) : BaseFragment<B>(bindingInflater) {
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
