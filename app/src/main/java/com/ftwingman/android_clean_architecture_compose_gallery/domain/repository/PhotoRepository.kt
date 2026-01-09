package com.ftwingman.android_clean_architecture_compose_gallery.domain.repository

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
     * @param page 頁碼
     * @param perPage 每頁數量
     * @return 返回圖片列表的 Flow
     */
    fun getPhotos(page: Int, perPage: Int): Flow<List<Photo>>
}
