package com.teamfilmo.filmo.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B : ViewBinding>(
    private val inflate: (LayoutInflater) -> B,
) : AppCompatActivity() {
    @Suppress("ktlint:standard:property-naming")
    protected inline val TAG: String
        get() = this::class.java.simpleName

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    protected open fun init() = Unit
}
