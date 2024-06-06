package com.teamfilmo.filmo.ui.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teamfilmo.filmo.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
