package com.ftwingman.android_clean_architecture_compose_gallery

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApplicationTest {
    @Test
    fun testApplicationIsMainApplication() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        assertTrue("Application context should be instance of MainApplication", appContext is MainApplication)
    }
}
