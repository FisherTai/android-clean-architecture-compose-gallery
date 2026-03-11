package com.ftwingman.android_clean_architecture_compose_gallery.model

/**
 * 通用媒體領域模型，支援圖片與影片混排。
 * 基於 Pexels API 設計，可擴充至其他富媒體來源。
 */
data class MediaItem(
    val id: String,
    val width: Int,
    val height: Int,
    /** 媒體類型：IMAGE 或 VIDEO */
    val mediaType: MediaType,
    /** 供 Coil 載入的靜態縮圖 URL（圖片本身或影片首圖） */
    val thumbnailUrl: String,
    /** 完整解析度圖片 URL（僅 IMAGE 類型有效） */
    val imageUrl: String?,
    /** 影片串流 URL（僅 VIDEO 類型有效） */
    val videoUrl: String?,
    /** 影片時長，單位秒（僅 VIDEO 類型有效） */
    val videoDuration: Int?,
    val description: String?,
    val photographer: String,
    val photographerUrl: String?,
    val avgColor: String?
)
