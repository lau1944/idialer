package com.vau.studio.iosstyle.idialer_phone

import android.app.Application
import com.vau.studio.iosstyle.idialer_phone.data.SHARED_PREFERENCE_KEY
import com.vau.studio.iosstyle.idialer_phone.data.local.SharePreferenceClient
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initLib()
    }

    private fun initLib() {
        SharePreferenceClient.init(context = applicationContext, SHARED_PREFERENCE_KEY)
    }
}