package com.ftwingman.android_clean_architecture_compose_gallery.domain.usecase

import com.ftwingman.android_clean_architecture_compose_gallery.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.model.User
import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.PhotoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetPhotoDetailUseCaseTest {

    private val repository = mockk<PhotoRepository>()
    private val useCase = GetPhotoDetailUseCase(repository)

    @Test
    fun `invoke should delegate to repository getPhotoById`() {
        // Given
        val id = "1"
        val photo = Photo(
            id = id,
            width = 100,
            height = 100,
            url = "url",
            thumbnailUrl = "thumb",
            color = "#000000",
            blurHash = null,
            description = "desc",
            author = User("u1", "user", "name", "profile"),
            exif = null,
            unsplashUrl = null,
            downloadUrl = null
        )
        every { repository.getPhotoById(id) } returns flowOf(photo)
        // When
        useCase(id)
        // Then
        verify { repository.getPhotoById(id) }
    }

    @Test
    fun `refresh should delegate to repository refreshPhotoDetail`() = runBlocking {
        // Given
        val id = "1"
        coEvery { repository.refreshPhotoDetail(id) } returns Unit
        // When
        useCase.refresh(id)
        // Then
        coVerify { repository.refreshPhotoDetail(id) }
    }
}