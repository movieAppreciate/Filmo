package com.teamfilmo.filmo.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.teamfilmo.filmo.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}
