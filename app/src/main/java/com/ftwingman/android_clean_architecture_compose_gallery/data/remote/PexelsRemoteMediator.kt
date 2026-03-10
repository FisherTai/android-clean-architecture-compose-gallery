package com.ftwingman.android_clean_architecture_compose_gallery.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.InfiniteMuseDatabase
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.MediaItemEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.MediaRemoteKeys
import com.ftwingman.android_clean_architecture_compose_gallery.data.mapper.toEntity
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaType
import timber.log.Timber

/**
 * Pexels 媒體列表的 RemoteMediator。
 * @param mediaType 指定載入圖片、影片或混合（null）。
 * @param scope 快取隔離維度（"IMAGE"、"VIDEO"、"MIXED"），確保不同列表模式的分頁狀態互不干擾。
 */
@OptIn(ExperimentalPagingApi::class)
class PexelsRemoteMediator(
    private val mediaType: MediaType?,
    private val scope: String,
    private val apiService: PexelsApiService,
    private val database: InfiniteMuseDatabase
) : RemoteMediator<Int, MediaItemEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MediaItemEntity>
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

            Timber.d("PexelsRemoteMediator: 載入第 $page 頁（scope: $scope, LoadType: $loadType）")

            val entities = when (mediaType) {
                MediaType.IMAGE -> {
                    val response = apiService.getCuratedPhotos(page, state.config.pageSize)
                    response.photos.map { it.toEntity(scope) }
                }
                MediaType.VIDEO -> {
                    val response = apiService.getPopularVideos(page, state.config.pageSize)
                    response.videos.map { it.toEntity(scope) }
                }
                null -> {
                    // 混合模式：同時取圖片與影片，交錯合併
                    // TODO: H1 — 目前圖片與影片共用同一個 page 變數追蹤分頁狀態，
                    //  若其中一個 API 較早到達末頁，另一個仍有資料時可能導致單邊重複載入。
                    //  後續應改為獨立分頁追蹤（imageNextPage / videoNextPage）。
                    val perType = state.config.pageSize / 2
                    val photos = apiService.getCuratedPhotos(page, perType).photos.map { it.toEntity(scope) }
                    val videos = apiService.getPopularVideos(page, perType).videos.map { it.toEntity(scope) }
                    // 交錯合併，確保圖片與影片穿插顯示
                    buildList {
                        val maxSize = maxOf(photos.size, videos.size)
                        for (i in 0 until maxSize) {
                            if (i < photos.size) add(photos[i])
                            if (i < videos.size) add(videos[i])
                        }
                    }
                }
            }

            val endOfPaginationReached = entities.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.mediaRemoteKeysDao().clearByScope(scope)
                    database.mediaItemDao().clearByScope(scope)
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = entities.map {
                    MediaRemoteKeys(scope = scope, mediaId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.mediaRemoteKeysDao().insertAll(keys)
                database.mediaItemDao().insertAll(entities)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Timber.e(e, "PexelsRemoteMediator: 載入失敗")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MediaItemEntity>): MediaRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { database.mediaRemoteKeysDao().remoteKeysBy(scope, it.id) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MediaItemEntity>): MediaRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {
                database.mediaRemoteKeysDao().remoteKeysBy(scope, it)
            }
        }
    }
}
