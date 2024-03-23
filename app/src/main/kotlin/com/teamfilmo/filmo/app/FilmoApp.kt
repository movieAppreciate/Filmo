package com.teamfilmo.filmo.app

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.teamfilmo.filmo.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FilmoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}
