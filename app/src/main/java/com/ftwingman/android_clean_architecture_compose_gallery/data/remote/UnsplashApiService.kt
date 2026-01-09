package com.ftwingman.android_clean_architecture_compose_gallery.data.remote

import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {

    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String = "latest"
    ): List<PhotoDto>

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
    }
}
