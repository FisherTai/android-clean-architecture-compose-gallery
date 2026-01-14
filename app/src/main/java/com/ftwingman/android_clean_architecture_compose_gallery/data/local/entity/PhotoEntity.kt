package com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    val id: String,
    val width: Int,
    val height: Int,
    val url: String,
    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String,
    val blurHash: String?,
    val description: String?,
    val authorName: String,
    val authorUsername: String,
    val authorProfileImage: String
)
