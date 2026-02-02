package com.ftwingman.android_clean_architecture_compose_gallery.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class PhotoModelTest {
    @Test
    fun testPhotoModelStructure() {
        val user = User(
            id = "user1",
            username = "photographer",
            name = "John Doe",
            profileImage = "https://example.com/profile.jpg"
        )
        val photo = Photo(
            id = "1",
            width = 100,
            height = 200,
            url = "url",
            thumbnailUrl = "thumb",
            color = "#000000",
            blurHash = "hash",
            description = "desc",
            author = user,
            exif = null
        )

        assertEquals("1", photo.id)
        assertEquals("photographer", photo.author.username)
    }
}
