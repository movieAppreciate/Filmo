package com.teamfilmo.filmo.app.initializer

import android.content.Context
import androidx.startup.Initializer
import com.kakao.sdk.common.KakaoSdk
import com.teamfilmo.filmo.R

class KakaoInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        KakaoSdk.init(
            context = context,
            appKey = context.getString(R.string.kakao_app_key),
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf()
    }
}
