package com.ftwingman.android_clean_architecture_compose_gallery.data.mapper

import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.MediaItemEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PexelsPhotoDto
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PexelsVideoDto
import com.ftwingman.android_clean_architecture_compose_gallery.model.MediaItem
import com.ftwingman.android_clean_architecture_compose_gallery.model.MediaType

// ─── Pexels Photo DTO → Entity ───────────────────────────────────────────────

fun PexelsPhotoDto.toEntity(scope: String): MediaItemEntity = MediaItemEntity(
    scope = scope,
    id = "photo_$id",
    width = width,
    height = height,
    mediaType = MediaType.IMAGE.name,
    thumbnailUrl = src.medium,
    imageUrl = src.large2x,
    videoUrl = null,
    videoDuration = null,
    description = alt,
    photographer = photographer,
    photographerUrl = photographerUrl,
    avgColor = avgColor
)

// ─── Pexels Video DTO → Entity ───────────────────────────────────────────────

fun PexelsVideoDto.toEntity(scope: String): MediaItemEntity {
    // 優先選 sd 品質的影片檔，若無則取第一個
    val videoFile = videoFiles.firstOrNull { it.quality == "sd" }
        ?: videoFiles.firstOrNull()

    return MediaItemEntity(
        scope = scope,
        id = "video_$id",
        width = width,
        height = height,
        mediaType = MediaType.VIDEO.name,
        thumbnailUrl = image,
        imageUrl = null,
        videoUrl = videoFile?.link,
        videoDuration = duration,
        description = null,
        photographer = user.name,
        photographerUrl = user.url,
        avgColor = null
    )
}

// ─── Entity → Domain Model ───────────────────────────────────────────────────

fun MediaItemEntity.toDomain(): MediaItem = MediaItem(
    id = id,
    width = width,
    height = height,
    mediaType = if (mediaType == MediaType.VIDEO.name) MediaType.VIDEO else MediaType.IMAGE,
    thumbnailUrl = thumbnailUrl,
    imageUrl = imageUrl,
    videoUrl = videoUrl,
    videoDuration = videoDuration,
    description = description,
    photographer = photographer,
    photographerUrl = photographerUrl,
    avgColor = avgColor
)
