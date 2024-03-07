package com.teamfilmo.filmo.util

import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.text.inSpans

inline fun SpannableStringBuilder.click(
    crossinline onClick: (View) -> Unit,
    @ColorInt color: Int,
    builderAction: SpannableStringBuilder.() -> Unit,
) = inSpans(
    object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = color
        }
    },
    builderAction = builderAction,
)
