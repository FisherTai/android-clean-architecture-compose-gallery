package com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity：Pexels 媒體項目（圖片或影片）。
 * 以 "media_type" 欄位區分，確保 Paging 快取正確運作。
 */
@Entity(tableName = "media_items")
data class MediaItemEntity(
    @PrimaryKey
    val id: String,
    val width: Int,
    val height: Int,
    /** "IMAGE" 或 "VIDEO" */
    @ColumnInfo(name = "media_type") val mediaType: String,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "video_url") val videoUrl: String?,
    @ColumnInfo(name = "video_duration") val videoDuration: Int?,
    val description: String?,
    val photographer: String,
    @ColumnInfo(name = "photographer_url") val photographerUrl: String?,
    @ColumnInfo(name = "avg_color") val avgColor: String?
)
