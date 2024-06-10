package com.teamfilmo.filmo.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.teamfilmo.filmo.databinding.WidgetAuthButtonBinding

class WidgetAuthButton
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private val binding: WidgetAuthButtonBinding =
            WidgetAuthButtonBinding.inflate(
                LayoutInflater.from(context),
                this,
                true,
            )

        fun setButtonText(text: String) {
            binding.title.text = text
        }

        fun setButtonTextColor(
            colorStateList: ColorStateList,
        ) {
            binding.title.setTextColor(colorStateList)
        }

        fun setButtonIcon(
            drawable: Drawable?,
        ) {
            binding.icon.setImageDrawable(drawable)
        }
    }

fun WidgetAuthButton.setButtonText(text: String) {
    setButtonText(text)
}

fun WidgetAuthButton.setButtonTextColor(
    colorStateList: ColorStateList,
) {
    setButtonTextColor(colorStateList)
}

fun WidgetAuthButton.setButtonIcon(
    drawable: Drawable?,
) {
    setButtonIcon(drawable)
}
