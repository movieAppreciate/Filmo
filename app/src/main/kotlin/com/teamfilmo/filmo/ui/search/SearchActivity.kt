package com.teamfilmo.filmo.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teamfilmo.filmo.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
