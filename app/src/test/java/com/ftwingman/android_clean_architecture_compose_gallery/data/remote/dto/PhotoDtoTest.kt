package com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class PhotoDtoTest {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Test
    fun `PhotoDto should be parsed correctly from JSON`() {
        val json = """
            {
                "id": "1",
                "width": 100,
                "height": 200,
                "description": "test description",
                "blur_hash": "test blur hash",
                "urls": {
                    "raw": "test raw",
                    "full": "test full",
                    "regular": "test url",
                    "small": "test small",
                    "thumb": "test thumb"
                },
                "user": {
                    "id": "u1",
                    "username": "testuser",
                    "name": "Test User",
                    "profile_image": {
                        "small": "test small",
                        "medium": "test profile image",
                        "large": "test large"
                    }
                }
            }
        """.trimIndent()

        val adapter = moshi.adapter(PhotoDto::class.java)
        val photoDto = adapter.fromJson(json)

        assertEquals("1", photoDto?.id)
        assertEquals(100, photoDto?.width)
        assertEquals(200, photoDto?.height)
        assertEquals("test description", photoDto?.description)
        assertEquals("test blur hash", photoDto?.blurHash)
        assertEquals("test url", photoDto?.urls?.regular)
        assertEquals("u1", photoDto?.user?.id)
        assertEquals("testuser", photoDto?.user?.username)
        assertEquals("Test User", photoDto?.user?.name)
        assertEquals("test profile image", photoDto?.user?.profileImage?.medium)
    }
}
