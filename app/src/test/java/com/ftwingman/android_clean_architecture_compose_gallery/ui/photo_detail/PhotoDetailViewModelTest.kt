package com.ftwingman.android_clean_architecture_compose_gallery.ui.photo_detail

import androidx.lifecycle.SavedStateHandle
import com.ftwingman.android_clean_architecture_compose_gallery.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.model.User
import com.ftwingman.android_clean_architecture_compose_gallery.domain.usecase.GetPhotoDetailUseCase
import io.mockk.coEvery
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

    private val getPhotoDetail = mockk<GetPhotoDetailUseCase>()
    private val testDispatcher = StandardTestDispatcher()
    private val photoId = "test_id"
    private val thumbnailUrl = "thumb_url"
    private val savedStateHandle = SavedStateHandle(mapOf("photoId" to photoId, "thumbnailUrl" to thumbnailUrl))

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
            color = "#000000",
            blurHash = null,
            description = "desc",
            author = User("u1", "user", "name", "profile"),
            exif = null,
            unsplashUrl = "html",
            downloadUrl = "download"
        )
        every { getPhotoDetail(photoId) } returns flowOf(photo)
        coEvery { getPhotoDetail.refresh(any()) } returns Unit

        // When
        val viewModel = PhotoDetailViewModel(getPhotoDetail, savedStateHandle)
        
        // StateFlow needs to be collected to start emissions when using WhileSubscribed
        backgroundScope.launch {
            viewModel.photo.collect {}
        }
        
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify { getPhotoDetail(photoId) }
        assertEquals(photo, viewModel.photo.value)
    }
}