package com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val photoId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
