package com.teamfilmo.filmo.app

import android.app.Application
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class FilmoApp : Application() {
    @Inject
    lateinit var timberTree: Timber.Tree

    override fun onCreate() {
        super.onCreate()

        Timber.plant(timberTree)
        Timber.d("App onCreate")

        NaverIdLoginSDK.showDevelopersLog(true)
    }
}
