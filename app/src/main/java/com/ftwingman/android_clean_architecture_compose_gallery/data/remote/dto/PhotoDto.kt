package com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoDto(
    val id: String,
    val width: Int,
    val height: Int,
    val description: String?,
    @Json(name = "blur_hash")
    val blurHash: String?,
    val urls: PhotoUrlsDto,
    val user: UserDto
)

@JsonClass(generateAdapter = true)
data class PhotoUrlsDto(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)
