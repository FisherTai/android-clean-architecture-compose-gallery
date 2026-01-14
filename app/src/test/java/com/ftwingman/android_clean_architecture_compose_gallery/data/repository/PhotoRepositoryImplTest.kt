package com.ftwingman.android_clean_architecture_compose_gallery.data.repository

import android.util.Log
import androidx.paging.PagingSource
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.InfiniteMuseDatabase
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.PhotoDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.PhotoEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.UnsplashApiService
import com.ftwingman.android_clean_architecture_compose_gallery.domain.repository.PhotoRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class PhotoRepositoryImplTest {

    private val apiService = mockk<UnsplashApiService>()
    private val database = mockk<InfiniteMuseDatabase>(relaxed = true)
    private val photoDao = mockk<PhotoDao>(relaxed = true)
    private lateinit var repository: PhotoRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.w(any(), any<Throwable>()) } returns 0
        every { Log.isLoggable(any(), any()) } returns false
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `getPhotos should return non-null PagingData flow`() = runBlocking {
        // Given
        every { database.photoDao() } returns photoDao
        // Mocking PagingSource is tricky, but Pager just needs it to exist
        every { photoDao.getPagingSource() } returns mockk<PagingSource<Int, PhotoEntity>>(relaxed = true)

        repository = PhotoRepositoryImpl(apiService, database)

        // When
        val result = repository.getPhotos().first()

                // Then

                // We verify that the repository correctly orchestrates the Pager creation

                // and emits a PagingData object. Detailed data content verification is handled

                // in Mediator and Mapper tests.

                assertNotNull(result)

            }

        

            @Test

            fun `getPhotoById should return mapped photo from DAO`() = runBlocking {

                // Given

                val id = "1"

                val entity = PhotoEntity(

                    id = id,

                                width = 100,

                                height = 100,

                                url = "url",

                                thumbnailUrl = "thumb",

                                blurHash = null,

                                description = "desc",

                                authorName = "author",

                    

                                authorUsername = "user",

                                authorProfileImage = "profile"

                            )

                            coEvery { database.photoDao().getPhotoById(any<String>()) } returns entity

                    

                            repository = PhotoRepositoryImpl(apiService, database)

                    

        

                // When

                val result = repository.getPhotoById(id).first()

        

                // Then

                assertNotNull(result)

                assertEquals(id, result?.id)

                assertEquals("desc", result?.description)

            }

        }

        