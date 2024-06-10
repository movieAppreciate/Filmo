package com.teamfilmo.filmo.base.util

import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String) {
    requireActivity().showToast(message)
}
