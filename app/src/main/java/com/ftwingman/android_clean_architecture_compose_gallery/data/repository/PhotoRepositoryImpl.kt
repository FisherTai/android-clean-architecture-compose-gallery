package com.ftwingman.android_clean_architecture_compose_gallery.data.repository

import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.PhotoDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.mapper.toDomain
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.UnsplashApiService
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val apiService: UnsplashApiService,
    private val photoDao: PhotoDao
) : PhotoRepository {

    override fun getPhotos(page: Int, perPage: Int): Flow<List<Photo>> = flow {
        val dtos = apiService.getPhotos(page, perPage)
        val domainPhotos = dtos.map { it.toDomain() }
        emit(domainPhotos)
    }
}
