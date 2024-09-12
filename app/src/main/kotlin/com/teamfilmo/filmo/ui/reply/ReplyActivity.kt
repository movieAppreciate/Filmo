package com.teamfilmo.filmo.ui.reply

import androidx.activity.viewModels
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityReplyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplyActivity : BaseActivity<ActivityReplyBinding, ReplyViewModel, ReplyEffect, ReplyEvent>(
    ActivityReplyBinding::inflate,
) {
    override val viewModel: ReplyViewModel by viewModels()
}
