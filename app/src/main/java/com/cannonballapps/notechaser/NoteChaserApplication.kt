package com.cannonballapps.notechaser

import android.app.Application
import timber.log.Timber

class NoteChaserApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}