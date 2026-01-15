package com.ftwingman.android_clean_architecture_compose_gallery.domain.repository

import androidx.paging.PagingData
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import kotlinx.coroutines.flow.Flow

/**
 * 圖片儲存庫介面。
 * 定義了 Domain 層對於圖片數據的操作需求。
 * 具體實作將由 Data 層提供。
 */
interface PhotoRepository {
    /**
     * 獲取圖片列表。
     * @return 返回圖片列表的分頁數據 Flow
     */
    fun getPhotos(): Flow<PagingData<Photo>>

    /**
     * 根據 ID 獲取圖片詳情。
     * @param id 圖片唯一識別碼
     * @return 返回圖片詳情的 Flow (若找不到則回傳 null)
     */
    fun getPhotoById(id: String): Flow<Photo?>

    /**
     * 強制從網路重新獲取圖片詳情並更新至資料庫。
     * @param id 圖片唯一識別碼
     */
    suspend fun refreshPhotoDetail(id: String)
}
