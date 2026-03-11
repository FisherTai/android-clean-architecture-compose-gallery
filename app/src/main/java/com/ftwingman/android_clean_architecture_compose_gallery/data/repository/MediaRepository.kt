package com.ftwingman.android_clean_architecture_compose_gallery.data.repository

import androidx.paging.PagingData
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaItem
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaType
import kotlinx.coroutines.flow.Flow

/**
 * 媒體儲存庫介面，支援圖片、影片與混合模式。
 */
interface MediaRepository {

    /**
     * 取得媒體列表。
     * @param mediaType 若為 null，回傳圖片與影片混合列表。
     */
    fun getMediaItems(mediaType: MediaType?): Flow<PagingData<MediaItem>>

    /**
     * 根據 ID 取得媒體項目詳情。
     */
    suspend fun getMediaItemById(id: String): MediaItem?
}
