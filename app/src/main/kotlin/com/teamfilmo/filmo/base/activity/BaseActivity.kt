package com.teamfilmo.filmo.base.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B : ViewBinding>(
    private val inflate: (LayoutInflater) -> B,
) : AppCompatActivity() {
    protected inline val TAG: String
        get() = this::class.java.simpleName

    protected lateinit var binding: B
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    protected open fun initViews() = Unit

    protected open fun initObserver() = Unit

    protected fun bind(block: B.() -> Unit) {
        binding.run(block)
    }
}
