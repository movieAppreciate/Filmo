package com.teamfilmo.filmo.base.activity

import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import com.teamfilmo.filmo.base.effect.BaseEffect
import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.base.util.repeatOnStarted
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel

abstract class BaseDataBindingActivity<B : ViewDataBinding, VM : BaseViewModel<EF, EV>, EF : BaseEffect, EV : BaseEvent>(
    bindingInflater: (layoutInflater: LayoutInflater) -> B,
) : BaseActivity<B>(bindingInflater) {
    protected abstract val viewModel: VM

    protected abstract val viewModelVariableId: Int?

    override fun initViews() {
        bind {
            lifecycleOwner = this@BaseDataBindingActivity
            viewModelVariableId?.let { variableId ->
                setVariable(variableId, viewModel)
            }
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
