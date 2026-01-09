package com.ftwingman.android_clean_architecture_compose_gallery

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

// Annotated with @HiltAndroidApp but not yet registered in Manifest
@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "[InfiniteMuse] ${super.createStackElementTag(element)}"
                }
            })
        }
    }
}