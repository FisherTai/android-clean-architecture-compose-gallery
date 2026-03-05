package com.ftwingman.android_clean_architecture_compose_gallery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.MediaItemDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.MediaRemoteKeysDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.PhotoDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.RemoteKeysDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.MediaItemEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.MediaRemoteKeys
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.PhotoEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.RemoteKeys

@Database(
    entities = [
        PhotoEntity::class,
        RemoteKeys::class,
        MediaItemEntity::class,
        MediaRemoteKeys::class
    ],
    version = 5,
    exportSchema = false
)
abstract class InfiniteMuseDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun mediaRemoteKeysDao(): MediaRemoteKeysDao
}
