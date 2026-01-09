package com.ftwingman.android_clean_architecture_compose_gallery.data.mapper

import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.PhotoEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PhotoDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PhotoUrlsDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.ProfileImageDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.UserDto
import org.junit.Assert.assertEquals
import org.junit.Test

class PhotoMapperTest {

    @Test
    fun `PhotoDto toDomain should map correctly`() {
        val dto = PhotoDto(
            id = "1",
            width = 100,
            height = 200,
            description = "desc",
            blurHash = "hash",
            urls = PhotoUrlsDto(raw = "", full = "", regular = "url", small = "", thumb = ""),
            user = UserDto(
                id = "u1",
                username = "username",
                name = "name",
                profileImage = ProfileImageDto(small = "", medium = "profile", large = "")
            )
        )

        val domain = dto.toDomain()

        assertEquals("1", domain.id)
        assertEquals(100, domain.width)
        assertEquals(200, domain.height)
        assertEquals("desc", domain.description)
        assertEquals("hash", domain.blurHash)
        assertEquals("url", domain.url)
        assertEquals("u1", domain.author.id)
        assertEquals("username", domain.author.username)
        assertEquals("name", domain.author.name)
        assertEquals("profile", domain.author.profileImage)
    }

    @Test
    fun `PhotoDto toEntity should map correctly`() {
        val dto = PhotoDto(
            id = "1",
            width = 100,
            height = 200,
            description = "desc",
            blurHash = "hash",
            urls = PhotoUrlsDto(raw = "", full = "", regular = "url", small = "", thumb = ""),
            user = UserDto(
                id = "u1",
                username = "username",
                name = "name",
                profileImage = ProfileImageDto(small = "", medium = "profile", large = "")
            )
        )

        val entity = dto.toEntity()

        assertEquals("1", entity.id)
        assertEquals(100, entity.width)
        assertEquals(200, entity.height)
        assertEquals("desc", entity.description)
        assertEquals("hash", entity.blurHash)
        assertEquals("url", entity.url)
        assertEquals("name", entity.authorName)
        assertEquals("username", entity.authorUsername)
        assertEquals("profile", entity.authorProfileImage)
    }

    @Test
    fun `PhotoEntity toDomain should map correctly`() {
        val entity = PhotoEntity(
            id = "1",
            width = 100,
            height = 200,
            url = "url",
            blurHash = "hash",
            description = "desc",
            authorName = "name",
            authorUsername = "username",
            authorProfileImage = "profile"
        )

        val domain = entity.toDomain()

        assertEquals("1", domain.id)
        assertEquals(100, domain.width)
        assertEquals(200, domain.height)
        assertEquals("desc", domain.description)
        assertEquals("hash", domain.blurHash)
        assertEquals("url", domain.url)
        assertEquals("", domain.author.id) // Entity doesn't store author ID
        assertEquals("username", domain.author.username)
        assertEquals("name", domain.author.name)
        assertEquals("profile", domain.author.profileImage)
    }
}
