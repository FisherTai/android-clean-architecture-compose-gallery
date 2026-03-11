package com.ftwingman.android_clean_architecture_compose_gallery.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.InfiniteMuseDatabase
import com.ftwingman.android_clean_architecture_compose_gallery.data.mapper.toDomain
import com.ftwingman.android_clean_architecture_compose_gallery.data.mapper.toEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.PhotoRemoteMediator
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.UnsplashApiService
import com.ftwingman.android_clean_architecture_compose_gallery.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PhotoRepositoryImpl @Inject constructor(
    private val apiService: UnsplashApiService,
    private val database: InfiniteMuseDatabase
) : PhotoRepository {

    override fun getPhotos(): Flow<PagingData<Photo>> {
        Timber.d("PhotoRepositoryImpl: creating Pager")
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true // BlurHash placeholders if UI supports
            ),
            remoteMediator = PhotoRemoteMediator(
                apiService = apiService,
                database = database
            ),
            pagingSourceFactory = { database.photoDao().getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getPhotoById(id: String): Flow<Photo?> = flow {
        val entity = database.photoDao().getPhotoById(id)
        emit(entity?.toDomain())
    }

    override suspend fun refreshPhotoDetail(id: String) {
        try {
            Timber.d("Refreshing photo detail for id: $id")
            val dto = apiService.getPhotoDetail(id)
            Timber.d("Fetched detail for $id. EXIF: ${dto.exif}")
            val entity = dto.toEntity()
            // We use insertAll (OnConflictStrategy.REPLACE) to update the existing record
            database.photoDao().insertAll(listOf(entity))
        } catch (e: Exception) {
            Timber.e(e, "Error refreshing photo detail for id: $id")
            // We suppress the error here as the UI will still show cached data
        }
    }
}
