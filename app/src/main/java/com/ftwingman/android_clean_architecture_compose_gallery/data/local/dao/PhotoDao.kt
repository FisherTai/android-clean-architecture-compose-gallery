package com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.PhotoEntity

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photos")
    fun getPagingSource(): PagingSource<Int, PhotoEntity>

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun getPhotoById(id: String): PhotoEntity?

    @Query("DELETE FROM photos")
    suspend fun clearAll()
}