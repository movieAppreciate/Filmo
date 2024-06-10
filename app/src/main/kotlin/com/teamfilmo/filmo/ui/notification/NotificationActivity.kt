package com.teamfilmo.filmo.ui.notification

import androidx.activity.viewModels
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : BaseActivity<ActivityNotificationBinding, NotificationViewModel, NotificationEffect, NotificationEvent>(
    ActivityNotificationBinding::inflate,
) {
    override val viewModel: NotificationViewModel by viewModels()

    override fun onBindLayout() {
    }

    override fun handleEffect(effect: NotificationEffect) {
    }
}
