# Infinite Muse - Android Clean Architecture Gallery

**Infinite Muse** 是一個展示 Android 現代化開發最佳實踐的圖片與影音瀏覽器 App。

本專案旨在演示如何在中型規模的應用中，透過 **Clean Architecture** 與 **MVVM** 模式，整合 **Paging 3**、**Room** 與 **Retrofit** 來實現穩健的「離線優先 (Offline-First)」架構，並結合 **Jetpack Compose** 與 **Material 3** 打造流暢且沉浸的使用者體驗。同時透過 **Media3 (ExoPlayer)** 實作影片預覽與播放功能。

### Architecture & Data (架構與數據)
*   **Clean Architecture**: 遵守分層原則 (Domain, Data, UI)，確保業務邏輯與框架解耦。
*   **Offline-First**: 透過 **Paging 3 RemoteMediator** 實作網路與本地資料庫的單一來源 (SSOT)，支援離線瀏覽。
*   **Dependency Injection**: 使用 **Hilt** 進行依賴注入。
*   **Reactive Data Flow**: 使用 **Kotlin Flow** 與 **Coroutines**。

### UI & UX (介面與體驗)
*   使用 **Jetpack Compose** + **Material Design 3**
*   實作 **Edge-to-Edge** 全螢幕瀏覽，圖片穿透狀態列。
*   支援列表至詳情頁的無縫 **共享元素轉場(Shared Element Transition)** 動畫 (Compose 1.7+)。
*   使用圖片色調作為載入前佔位符，並實作點擊微交互。
*   支援深色/淺色模式切換及動態取色。(Android 12+)

### Video Preview (影音預覽)
*   整合 **Pexels API** 提供影片與圖片混合瀏覽，支援僅圖片、僅影片、混合三種模式。
*   使用 **Media3 (ExoPlayer)** 實作列表內影片自動播放，滾動偵測最靠近畫面中心的影片並切換播放。
*   影片播放支援 **200MB LRU 快取**，詳情頁與列表頁共用快取達成快速載入。
*   實作縮圖到播放的淡出過渡效果，以及播放器生命週期管理（頁面不可見時暫停）。
*   透過 **scope 隔離** 解決多列表間分頁資料衝突。

## Tech Stack (技術棧)

*   **Language**: Kotlin
*   **UI Toolkit**: Jetpack Compose (Material 3)
*   **Navigation**: Navigation Compose (Type-safe)
*   **Async Image**: Coil
*   **DI**: Hilt
*   **Network**: Retrofit, OkHttp, Moshi
*   **Database**: Room
*   **Pagination**: Paging 3
*   **Video**: Media3 (ExoPlayer, Cache, DataSource)
*   **Testing**: JUnit 4, Mockk, Kotlinx Coroutines Test

## Architecture Overview (架構圖)

```mermaid  graph TD  
graph TD
    subgraph UI_Layer
        UI[Compose Screens] --> VM[ViewModel]
    end

    subgraph Domain_Layer
        VM -.-> UC["UseCase(可選)"]
    end

    subgraph Data_Layer
        UC --> Repo[Repository Interface]
        VM --> Repo

        RepoImpl[Repository Impl] -->|implements| Repo
        RepoImpl --> Mediator[RemoteMediator]
        RepoImpl --> DAO[Room DAO]
        DAO --> DB[Room Database]

        Mediator --> API[Retrofit Service]
        Mediator --> DB
    end

    DB -- PagingSource --> RepoImpl
    RepoImpl -- PagingData Flow --> UC
    RepoImpl -- PagingData Flow --> VM
```

## Setup & Build (如何執行)

本專案使用 [Unsplash Developers](https://unsplash.com/developers) 與 [Pexels API](https://www.pexels.com/api/) 的 API。

1.  在專案根目錄建立或開啟 `local.properties` 檔案。
2.  新增以下設定：

```properties
UNSPLASH_ACCESS_KEY=您的_Unsplash_API_KEY
PEXELS_ACCESS_KEY=您的_Pexels_API_KEY
```

3.  Sync Gradle 並執行專案。
