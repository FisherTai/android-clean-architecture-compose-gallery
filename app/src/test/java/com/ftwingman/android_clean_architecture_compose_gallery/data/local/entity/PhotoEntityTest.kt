package com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity

import org.junit.Assert.assertEquals
import org.junit.Test

class PhotoEntityTest {

    @Test
    fun `PhotoEntity should store photo data correctly`() {
        val entity = PhotoEntity(
            id = "1",
            width = 100,
            height = 200,
            url = "url",
            blurHash = "hash",
            description = "desc",
            authorName = "author",
            authorUsername = "username",
            authorProfileImage = "profile"
        )

        assertEquals("1", entity.id)
        assertEquals(100, entity.width)
        assertEquals(200, entity.height)
        assertEquals("url", entity.url)
        assertEquals("hash", entity.blurHash)
        assertEquals("desc", entity.description)
        assertEquals("author", entity.authorName)
        assertEquals("username", entity.authorUsername)
        assertEquals("profile", entity.authorProfileImage)
    }

    @Test
    fun `RemoteKeys should store pagination data correctly`() {
        val remoteKeys = RemoteKeys(
            photoId = "1",
            prevKey = 1,
            nextKey = 3
        )

        assertEquals("1", remoteKeys.photoId)
        assertEquals(1, remoteKeys.prevKey)
        assertEquals(3, remoteKeys.nextKey)
    }
}
