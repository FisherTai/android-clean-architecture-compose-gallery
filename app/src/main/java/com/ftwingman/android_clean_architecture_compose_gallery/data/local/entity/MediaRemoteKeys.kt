package com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Pexels 媒體列表分頁的 RemoteKeys。
 * 以 (scope, mediaId) 為組合主鍵，確保不同列表模式的分頁狀態互相隔離。
 */
@Entity(
    tableName = "media_remote_keys",
    primaryKeys = ["scope", "media_id"]
)
data class MediaRemoteKeys(
    val scope: String,
    @ColumnInfo(name = "media_id") val mediaId: String,
    @ColumnInfo(name = "prev_key") val prevKey: Int?,
    @ColumnInfo(name = "next_key") val nextKey: Int?
)
