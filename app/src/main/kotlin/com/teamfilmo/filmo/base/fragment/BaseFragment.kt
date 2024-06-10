package com.teamfilmo.filmo.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.teamfilmo.filmo.base.effect.BaseEffect
import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.base.util.repeatOnStarted
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel

abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel<EF, EV>, EF : BaseEffect, EV : BaseEvent>(
    private val inflater: (LayoutInflater, ViewGroup?, Boolean) -> B,
) : Fragment() {
    protected inline val TAG: String
        get() = this::class.java.simpleName

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: B? = null
    protected val binding
        get() = requireNotNull(_binding)

    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        onBindLayout()
        onBindObserver()
    }

    protected open fun onBindLayout() = Unit

    private fun onBindObserver() {
        repeatOnStarted {
            viewModel.effect.collect {
                handleEffect(it)
            }
        }
    }

    protected open fun handleEffect(effect: EF) = Unit

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun bind(block: B.() -> Unit) {
        binding.run(block)
    }
}
