package com.ftwingman.android_clean_architecture_compose_gallery.domain.model

/**
 * 代表圖片的領域模型。
 * 這是純 Kotlin 類別，不依賴於任何 Data Layer (DTO) 或 UI Layer 的實作。
 */
data class Photo(
    val id: String,
    val width: Int,
    val height: Int,
    val url: String,
    val thumbnailUrl: String,
    val color: String?,
    val blurHash: String?,
    val description: String?,
    val author: User,
    val exif: ExifInfo?
)
