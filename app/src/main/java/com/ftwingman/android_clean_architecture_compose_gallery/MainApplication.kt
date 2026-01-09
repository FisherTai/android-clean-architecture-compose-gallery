package com.ftwingman.android_clean_architecture_compose_gallery

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Annotated with @HiltAndroidApp but not yet registered in Manifest
@HiltAndroidApp
class MainApplication : Application()