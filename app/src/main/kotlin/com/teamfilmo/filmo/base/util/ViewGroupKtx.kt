package com.teamfilmo.filmo.base.util

import android.view.LayoutInflater
import android.view.ViewGroup

inline val ViewGroup.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)
