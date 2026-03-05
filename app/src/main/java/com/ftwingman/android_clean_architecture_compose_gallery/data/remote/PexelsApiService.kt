package com.ftwingman.android_clean_architecture_compose_gallery.data.remote

import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PexelsPhotoResponseDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PexelsVideoResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApiService {

    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20
    ): PexelsPhotoResponseDto

    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20
    ): PexelsVideoResponseDto

    @GET("v1/search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20
    ): PexelsPhotoResponseDto

    @GET("videos/search")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20
    ): PexelsVideoResponseDto

    companion object {
        const val BASE_URL = "https://api.pexels.com/"
    }
}
