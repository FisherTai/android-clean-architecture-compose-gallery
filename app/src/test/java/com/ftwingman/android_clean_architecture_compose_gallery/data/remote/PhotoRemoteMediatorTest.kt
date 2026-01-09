package com.ftwingman.android_clean_architecture_compose_gallery.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.InfiniteMuseDatabase
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.PhotoDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.RemoteKeysDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.PhotoEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PhotoDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PhotoUrlsDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.ProfileImageDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.UserDto
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediatorTest {

    private val apiService = mockk<UnsplashApiService>()
    private val database = mockk<InfiniteMuseDatabase>(relaxed = true)
    private val photoDao = mockk<PhotoDao>(relaxed = true)
    private val remoteKeysDao = mockk<RemoteKeysDao>(relaxed = true)

    @Before
    fun setup() {
        coEvery { database.photoDao() } returns photoDao
        coEvery { database.remoteKeysDao() } returns remoteKeysDao
        
        // Mock withTransaction to just execute the block immediately
        mockkStatic("androidx.room.RoomDatabaseKt")
        val lambda = slot<suspend () -> Any>()
        coEvery { database.withTransaction(capture(lambda)) } coAnswers {
            lambda.captured.invoke()
        }
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `refresh load should return success result when more data is present`() = runBlocking {
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

        val mediator = PhotoRemoteMediator(apiService, database)
        val pagingState = PagingState<Int, PhotoEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(10),
            leadingPlaceholderCount = 0
        )

        // When
        val result = mediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(!(result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }
}
