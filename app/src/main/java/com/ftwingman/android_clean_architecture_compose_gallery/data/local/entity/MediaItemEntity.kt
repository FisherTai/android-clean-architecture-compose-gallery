package com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Room Entity：Pexels 媒體項目（圖片或影片）。
 * 以 (scope, id) 為組合主鍵，確保不同列表模式的快取資料互相隔離。
 * scope 值為 "IMAGE"、"VIDEO" 或 "MIXED"，代表載入此項目的列表模式。
 */
@Entity(
    tableName = "media_items",
    primaryKeys = ["scope", "id"]
)
data class MediaItemEntity(
    val scope: String,
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
