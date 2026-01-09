package com.ftwingman.android_clean_architecture_compose_gallery.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.InfiniteMuseDatabase
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.PhotoEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.RemoteKeys
import com.ftwingman.android_clean_architecture_compose_gallery.data.mapper.toEntity
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator(
    private val apiService: UnsplashApiService,
    private val database: InfiniteMuseDatabase
) : RemoteMediator<Int, PhotoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            Timber.d("PhotoRemoteMediator: Loading page $page (LoadType: $loadType)")

            // We will make this call in a blocking way within the coroutine scope of load
            // Retrofit suspend functions handle thread switching
            val photos = apiService.getPhotos(page = page, perPage = state.config.pageSize)
            val endOfPaginationReached = photos.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.photoDao().clearAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = photos.map {
                    RemoteKeys(photoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.photoDao().insertAll(photos.map { it.toEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Timber.e(e, "PhotoRemoteMediator: Error during load")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PhotoEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { photo ->
                database.remoteKeysDao().remoteKeysPhotoId(photo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PhotoEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { photoId ->
                database.remoteKeysDao().remoteKeysPhotoId(photoId)
            }
        }
    }
}
