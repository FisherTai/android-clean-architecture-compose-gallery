package com.ftwingman.android_clean_architecture_compose_gallery.domain.usecase

import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 獲取圖片詳情的 UseCase (Interactor)。
 *
 * ## Clean Architecture 設計思維 (Design Rationale)
 * 在 Clean Architecture 中，UseCase 層負責封裝特定的業務邏輯 (Business Rules)。
 * 它作為 Presentation Layer (ViewModel) 與 Data Layer (Repository) 之間的中介者。
 *
 * ### 為什麼在這個簡單專案中引入 UseCase？
 * 雖然 `getPhotoDetail` 的邏輯目前僅是單純轉發 Repository 的呼叫 (Pass-through)，
 * 但引入 UseCase 有以下架構優勢：
 * 1.  **解耦 (Decoupling)**: ViewModel 不需要知道資料是來自哪個 Repository，也不受 Repository 介面變動的直接影響。
 * 2.  **可測試性 (Testability)**: 業務邏輯可以在不依賴 Android Framework 的情況下進行單元測試。
 * 3.  **可擴充性 (Scalability)**: 如果未來獲取詳情的邏輯變複雜（例如：同時需要從 User Repository 獲取更多作者資訊並合併），
 *     我們只需修改 UseCase，而無需改動 ViewModel。
 *
 * 此實作展示了對架構完整性的堅持，即使在 CRUD 類型的操作中，也保持層級邊界的清晰。
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
