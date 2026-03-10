package com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.MediaItemEntity

@Dao
interface MediaItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MediaItemEntity>)

    @Query("SELECT * FROM media_items WHERE scope = :scope ORDER BY rowid ASC")
    fun pagingSourceByScope(scope: String): PagingSource<Int, MediaItemEntity>

    @Query("SELECT * FROM media_items WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): MediaItemEntity?

    @Query("DELETE FROM media_items WHERE scope = :scope")
    suspend fun clearByScope(scope: String)

    @Query("DELETE FROM media_items")
    suspend fun clearAll()
}
