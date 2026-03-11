package com.ftwingman.android_clean_architecture_compose_gallery.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.InfiniteMuseDatabase
import com.ftwingman.android_clean_architecture_compose_gallery.data.mapper.toDomain
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.PexelsApiService
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.PexelsRemoteMediator
import com.ftwingman.android_clean_architecture_compose_gallery.model.MediaItem
import com.ftwingman.android_clean_architecture_compose_gallery.model.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val apiService: PexelsApiService,
    private val database: InfiniteMuseDatabase
) : MediaRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMediaItems(mediaType: MediaType?): Flow<PagingData<MediaItem>> {
        val scope = when (mediaType) {
            MediaType.IMAGE -> "IMAGE"
            MediaType.VIDEO -> "VIDEO"
            null -> "MIXED"
        }

        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = PexelsRemoteMediator(
                mediaType = mediaType,
                scope = scope,
                apiService = apiService,
                database = database
            ),
            pagingSourceFactory = {
                database.mediaItemDao().pagingSourceByScope(scope)
            }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getMediaItemById(id: String): MediaItem? {
        return database.mediaItemDao().getById(id)?.toDomain()
    }
}
