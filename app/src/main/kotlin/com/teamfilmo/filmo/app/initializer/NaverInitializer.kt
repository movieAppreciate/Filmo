package com.teamfilmo.filmo.app.initializer

import android.content.Context
import androidx.startup.Initializer
import com.navercorp.nid.NaverIdLoginSDK
import com.teamfilmo.filmo.R

class NaverInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        NaverIdLoginSDK.initialize(
            context = context,
            clientId = context.getString(R.string.naver_client_id),
            clientSecret = context.getString(R.string.naver_client_secret),
            clientName = context.getString(R.string.naver_client_name),
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf()
    }
}
