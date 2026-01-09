package com.ftwingman.android_clean_architecture_compose_gallery.di

import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.UnsplashApiService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class NetworkModuleTest {

    @Test
    fun `provideMoshi should return non-null Moshi`() {
        val moshi = NetworkModule.provideMoshi()
        assertNotNull(moshi)
    }

    @Test
    fun `provideOkHttpClient should return non-null OkHttpClient`() {
        val client = NetworkModule.provideOkHttpClient()
        assertNotNull(client)
    }

    @Test
    fun `provideRetrofit should return non-null Retrofit with correct base URL`() {
        val moshi = NetworkModule.provideMoshi()
        val client = NetworkModule.provideOkHttpClient()
        val retrofit = NetworkModule.provideRetrofit(client, moshi)
        assertNotNull(retrofit)
        assertEquals(UnsplashApiService.BASE_URL, retrofit.baseUrl().toString())
    }

    @Test
    fun `provideUnsplashApiService should return non-null service`() {
        val moshi = NetworkModule.provideMoshi()
        val client = NetworkModule.provideOkHttpClient()
        val retrofit = NetworkModule.provideRetrofit(client, moshi)
        val service = NetworkModule.provideUnsplashApiService(retrofit)
        assertNotNull(service)
    }
}
