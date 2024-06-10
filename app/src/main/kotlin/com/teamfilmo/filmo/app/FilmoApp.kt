package com.teamfilmo.filmo.app

import android.app.Application
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FilmoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.d("App onCreate")

        NaverIdLoginSDK.showDevelopersLog(true)
    }
}
