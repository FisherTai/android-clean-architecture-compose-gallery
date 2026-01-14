package com.ftwingman.android_clean_architecture_compose_gallery

import android.app.Application

import coil.ImageLoader

import coil.ImageLoaderFactory

import coil.util.DebugLogger

import dagger.hilt.android.HiltAndroidApp

import timber.log.Timber



// Annotated with @HiltAndroidApp but not yet registered in Manifest

@HiltAndroidApp

class MainApplication : Application(), ImageLoaderFactory {

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



    override fun newImageLoader(): ImageLoader {

        return ImageLoader.Builder(this)

            .logger(DebugLogger())

            .build()

    }

}
