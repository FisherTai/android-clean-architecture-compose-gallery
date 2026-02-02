package com.ftwingman.android_clean_architecture_compose_gallery

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BuildConfigTest {
    @Test
    fun testUnsplashAccessKeyExists() {
        // This reference will cause a compilation error until the field is added
        val apiKey = BuildConfig.UNSPLASH_ACCESS_KEY
        assertNotNull("API Key should not be null", apiKey)
        assertTrue("API Key should not be empty", apiKey.isNotEmpty())
    }
}
