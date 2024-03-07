package com.teamfilmo.filmo.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.teamfilmo.filmo.databinding.WidgetAuthButtonBinding

@BindingMethods(
    value = [
        BindingMethod(
            type = WidgetAuthButton::class,
            attribute = "bind:background",
            method = "setBackground",
        ),
        BindingMethod(
            type = WidgetAuthButton::class,
            attribute = "bind:buttonText",
            method = "setButtonText",
        ),
        BindingMethod(
            type = WidgetAuthButton::class,
            attribute = "bind:buttonTextColor",
            method = "setButtonTextColor",
        ),
        BindingMethod(
            type = WidgetAuthButton::class,
            attribute = "bind:buttonIcon",
            method = "setButtonIcon",
        ),
    ],
)
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
            @ColorInt color: Int,
        ) {
            binding.title.setTextColor(color)
        }

        fun setButtonIcon(
            drawable: Drawable,
        ) {
            binding.icon.setImageDrawable(drawable)
        }

        fun setBackground(
            @DrawableRes background: Int,
        ) {
            binding.root.setBackgroundResource(background)
        }
    }
