package com.ftwingman.android_clean_architecture_compose_gallery.domain.usecase

import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Domain Layer(Optional)
 *
 * 在 Google 官方的現代架構中，Domain 層是非必需的。
 * 對於單純的 CRUD（從資料庫或 API 獲取資料並顯示），ViewModel 應該直接呼叫 Repository，避免過度設計。
 * 通常用途為以下
 * 1. 封裝複雜的商業邏輯
 * 2. 組合多個資料來源
 * 3. 避免邏輯重複
 * 此類別僅供展示「若需要 UseCase 時該如何實作與注入」。
 */
class GetPhotoDetailUseCase @Inject constructor(
    private val repository: PhotoRepository
) {

    /**
     * 執行 UseCase 獲取圖片詳情資料流。
     * 這裡利用 Kotlin 的 `operator fun invoke` 讓 UseCase 的呼叫看起來像是一個函數調用。
     */
    operator fun invoke(id: String): Flow<Photo?> {
        return repository.getPhotoById(id)
    }

    /**
     * 觸發詳情刷新。
     * 這是一個掛起函數，ViewModel 可以選擇性地呼叫它來強制更新資料。
     */
    suspend fun refresh(id: String) {
        repository.refreshPhotoDetail(id)
    }
}
