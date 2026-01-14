package com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_detail

import androidx.lifecycle.SavedStateHandle
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.User
import com.ftwingman.android_clean_architecture_compose_gallery.domain.repository.PhotoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoDetailViewModelTest {

    private val repository = mockk<PhotoRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private val photoId = "test_id"
    private val savedStateHandle = SavedStateHandle(mapOf("photoId" to photoId))

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel should fetch photo by id on initialization`() = runTest {
        // Given
        val photo = Photo(
            id = photoId,
            width = 100,
            height = 100,
            url = "url",
            thumbnailUrl = "thumb",
            blurHash = null,
            description = "desc",
            author = User("u1", "user", "name", "profile")
        )
        every { repository.getPhotoById(photoId) } returns flowOf(photo)

        // When
        val viewModel = PhotoDetailViewModel(repository, savedStateHandle)
        
        // StateFlow needs to be collected to start emissions when using WhileSubscribed
        backgroundScope.launch {
            viewModel.photo.collect {}
        }
        
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify { repository.getPhotoById(photoId) }
        assertEquals(photo, viewModel.photo.value)
    }
}