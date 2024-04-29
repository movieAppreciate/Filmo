package com.teamfilmo.filmo.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding>(
    private val inflater: (LayoutInflater, ViewGroup?, Boolean) -> B,
) : Fragment() {
    @Suppress("ktlint:standard:property-naming")
    protected inline val TAG: String
        get() = this::class.java.simpleName

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: B? = null
    protected val binding
        get() = requireNotNull(_binding)

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
        initViews()
        initObserver()
    }

    protected open fun initViews() = Unit

    protected open fun initObserver() = Unit

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun bind(block: B.() -> Unit) {
        binding.run(block)
    }
}
