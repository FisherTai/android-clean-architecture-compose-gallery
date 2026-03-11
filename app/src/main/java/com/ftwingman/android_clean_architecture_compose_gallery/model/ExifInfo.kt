package com.ftwingman.android_clean_architecture_compose_gallery.model

data class ExifInfo(
    val make: String?,
    val model: String?,
    val exposureTime: String?,
    val aperture: String?,
    val focalLength: String?,
    val iso: Int?
)
