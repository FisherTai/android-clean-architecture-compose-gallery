package com.ftwingman.android_clean_architecture_compose_gallery.ui.photo_list

import androidx.paging.PagingData
import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.PhotoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoListViewModelTest {

    private val repository = mockk<PhotoRepository>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel should fetch photos from repository on initialization`() {
        // Given
        every { repository.getPhotos() } returns flowOf(PagingData.empty())

        // When
        val viewModel = PhotoListViewModel(repository)

        // Then
        verify { repository.getPhotos() }
    }
}
