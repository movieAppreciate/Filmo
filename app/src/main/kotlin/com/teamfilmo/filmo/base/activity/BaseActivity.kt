package com.teamfilmo.filmo.base.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.teamfilmo.filmo.base.effect.BaseEffect
import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.base.util.repeatOnStarted
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel

abstract class BaseActivity<B : ViewBinding, VM : BaseViewModel<EF, EV>, EF : BaseEffect, EV : BaseEvent>(
    private val inflate: (LayoutInflater) -> B,
) : AppCompatActivity() {
    protected inline val TAG: String
        get() = this::class.java.simpleName

    protected lateinit var binding: B
        private set

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflate(layoutInflater)
        setContentView(binding.root)

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

    protected fun bind(block: B.() -> Unit) {
        binding.run(block)
    }
}
