package com.ftwingman.android_clean_architecture_compose_gallery.data.repository

import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.PhotoDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.UnsplashApiService
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PhotoDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PhotoUrlsDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.ProfileImageDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.UserDto
import com.ftwingman.android_clean_architecture_compose_gallery.domain.repository.PhotoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class PhotoRepositoryImplTest {

    private val apiService = mockk<UnsplashApiService>()
    private val photoDao = mockk<PhotoDao>(relaxed = true)
    private lateinit var repository: PhotoRepository

    @Test
    fun `getPhotos should return list of photos from API`() = runBlocking {
        // Given
        val photoDto = PhotoDto(
            id = "1",
            width = 100,
            height = 200,
            description = "desc",
            blurHash = "hash",
            urls = PhotoUrlsDto("raw", "full", "reg", "small", "thumb"),
            user = UserDto("u1", "user", "name", ProfileImageDto("s", "m", "l"))
        )
        coEvery { apiService.getPhotos(any(), any()) } returns listOf(photoDto)

        repository = PhotoRepositoryImpl(apiService, photoDao)

        // When
        val result = repository.getPhotos(1, 10).first()

        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("reg", result[0].url)
    }
}
