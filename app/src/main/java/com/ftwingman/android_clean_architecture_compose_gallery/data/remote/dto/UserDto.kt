package com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: String,
    val username: String,
    val name: String,
    @Json(name = "profile_image")
    val profileImage: ProfileImageDto
)

@JsonClass(generateAdapter = true)
data class ProfileImageDto(
    val small: String,
    val medium: String,
    val large: String
)
