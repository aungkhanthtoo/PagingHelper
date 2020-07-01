package com.dev.droid

import android.app.Application
import android.content.Context

/**
 * Created with love by A.K.HTOO on 28/06/2020,June,2020.
 */
class SampleApp : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext;
    }
}