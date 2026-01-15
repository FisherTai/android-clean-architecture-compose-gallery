package com.ftwingman.android_clean_architecture_compose_gallery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.PhotoDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.RemoteKeysDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.PhotoEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.RemoteKeys

@Database(
    entities = [PhotoEntity::class, RemoteKeys::class],
    version = 4,
    exportSchema = false
)
abstract class InfiniteMuseDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
