package com.ftwingman.android_clean_architecture_compose_gallery.data.remote

import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PhotoDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class UnsplashApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: UnsplashApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(UnsplashApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getPhotos should return list of photos`() = runBlocking {
        val json = """
            [
                {
                    "id": "1",
                    "width": 100,
                    "height": 200,
                    "description": "test",
                    "blur_hash": "hash",
                    "urls": {
                        "raw": "raw",
                        "full": "full",
                        "regular": "reg",
                        "small": "small",
                        "thumb": "thumb"
                    },
                    "user": {
                        "id": "u1",
                        "username": "user",
                        "name": "name",
                        "profile_image": {
                            "small": "s",
                            "medium": "m",
                            "large": "l"
                        }
                    }
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(200))

        val photos = apiService.getPhotos(page = 1, perPage = 10)

        assertEquals(1, photos.size)
        assertEquals("1", photos[0].id)
        
        val request = mockWebServer.takeRequest()
        assertEquals("/photos?page=1&per_page=10&order_by=latest", request.path)
    }
}
