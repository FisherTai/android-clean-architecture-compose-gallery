package com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PexelsVideoDto(
    @Json(name = "id") val id: Int,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "duration") val duration: Int,
    @Json(name = "user") val user: PexelsVideoUserDto,
    @Json(name = "image") val image: String,
    @Json(name = "video_files") val videoFiles: List<PexelsVideoFileDto>
)

@JsonClass(generateAdapter = true)
data class PexelsVideoUserDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String?
)

@JsonClass(generateAdapter = true)
data class PexelsVideoFileDto(
    @Json(name = "id") val id: Int,
    @Json(name = "quality") val quality: String,
    @Json(name = "file_type") val fileType: String,
    @Json(name = "width") val width: Int?,
    @Json(name = "height") val height: Int?,
    @Json(name = "link") val link: String
)

@JsonClass(generateAdapter = true)
data class PexelsVideoResponseDto(
    @Json(name = "videos") val videos: List<PexelsVideoDto>,
    @Json(name = "page") val page: Int,
    @Json(name = "per_page") val perPage: Int,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "next_page") val nextPage: String?
)
