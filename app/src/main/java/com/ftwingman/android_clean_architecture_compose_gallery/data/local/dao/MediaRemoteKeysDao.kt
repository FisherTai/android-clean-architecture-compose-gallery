package com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.MediaRemoteKeys

@Dao
interface MediaRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<MediaRemoteKeys>)

    @Query("SELECT * FROM media_remote_keys WHERE media_id = :mediaId")
    suspend fun remoteKeysByMediaId(mediaId: String): MediaRemoteKeys?

    @Query("DELETE FROM media_remote_keys")
    suspend fun clearAll()
}
