package com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PexelsPhotoDto(
    @Json(name = "id") val id: Int,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "alt") val alt: String?,
    @Json(name = "avg_color") val avgColor: String?,
    @Json(name = "photographer") val photographer: String,
    @Json(name = "photographer_url") val photographerUrl: String?,
    @Json(name = "src") val src: PexelsPhotoSrcDto
)

@JsonClass(generateAdapter = true)
data class PexelsPhotoSrcDto(
    @Json(name = "original") val original: String,
    @Json(name = "large2x") val large2x: String,
    @Json(name = "large") val large: String,
    @Json(name = "medium") val medium: String,
    @Json(name = "small") val small: String,
    @Json(name = "portrait") val portrait: String,
    @Json(name = "landscape") val landscape: String,
    @Json(name = "tiny") val tiny: String
)

@JsonClass(generateAdapter = true)
data class PexelsPhotoResponseDto(
    @Json(name = "photos") val photos: List<PexelsPhotoDto>,
    @Json(name = "page") val page: Int,
    @Json(name = "per_page") val perPage: Int,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "next_page") val nextPage: String?
)
