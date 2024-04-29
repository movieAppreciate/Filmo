package com.teamfilmo.filmo.base.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.teamfilmo.filmo.base.effect.BaseEffect
import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.base.util.repeatOnStarted
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel

abstract class BaseDataBindingFragment<B : ViewDataBinding, VM : BaseViewModel<EF, EV>, EF : BaseEffect, EV : BaseEvent>(
    bindingInflater: (layoutInflater: LayoutInflater, viewGroup: ViewGroup?, attachToParent: Boolean) -> B,
) : BaseFragment<B>(bindingInflater) {
    protected abstract val viewModel: VM

    protected abstract val viewModelVariableId: Int?

    override fun initViews() {
        bind {
            lifecycleOwner = viewLifecycleOwner
            viewModelVariableId?.let { id -> setVariable(id, viewModel) }
        }
    }

    override fun initObserver() {
        repeatOnStarted {
            viewModel.effect.collect {
                initEffectObserver(it)
            }
        }
    }

    protected open fun initEffectObserver(effect: EF) = Unit
}
