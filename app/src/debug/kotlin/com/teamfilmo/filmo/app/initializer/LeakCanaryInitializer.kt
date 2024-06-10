package com.teamfilmo.filmo.app.initializer

import android.content.Context
import androidx.startup.Initializer
import com.facebook.flipper.plugins.leakcanary2.FlipperLeakEventListener
import leakcanary.LeakCanary
import timber.log.Timber

class LeakCanaryInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.d("LeakCanaryInitializer", "LeakCanaryInitializer create")
        LeakCanary.config =
            LeakCanary.config.run {
                copy(eventListeners = eventListeners + FlipperLeakEventListener())
            }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
