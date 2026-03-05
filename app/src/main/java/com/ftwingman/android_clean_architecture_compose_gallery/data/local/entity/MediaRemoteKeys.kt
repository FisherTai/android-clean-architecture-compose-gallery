package com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Pexels 媒體列表分頁的 RemoteKeys。
 */
@Entity(tableName = "media_remote_keys")
data class MediaRemoteKeys(
    @PrimaryKey
    @ColumnInfo(name = "media_id") val mediaId: String,
    @ColumnInfo(name = "prev_key") val prevKey: Int?,
    @ColumnInfo(name = "next_key") val nextKey: Int?
)
