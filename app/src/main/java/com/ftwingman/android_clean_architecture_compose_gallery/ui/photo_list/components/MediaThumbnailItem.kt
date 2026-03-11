package com.ftwingman.android_clean_architecture_compose_gallery.ui.photo_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ftwingman.android_clean_architecture_compose_gallery.model.MediaItem
import com.ftwingman.android_clean_architecture_compose_gallery.ui.media_list.components.MediaCardWrapper
import com.ftwingman.android_clean_architecture_compose_gallery.ui.media_list.components.parseAvgColor

/**
 * Pexels 圖片項目卡片。
 * 使用 [MediaCardWrapper] 共用外殼，與影片卡片擁有一致的視覺與互動體驗。
 */
@Composable
fun MediaThumbnailItem(
    mediaItem: MediaItem,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val aspectRatio = if (mediaItem.width > 0 && mediaItem.height > 0) {
        mediaItem.width.toFloat() / mediaItem.height.toFloat()
    } else 1f

    val backgroundColor = parseAvgColor(mediaItem.avgColor)

    MediaCardWrapper(
        mediaItem = mediaItem,
        onItemClick = onItemClick,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(mediaItem.thumbnailUrl)
                .crossfade(true)
                .build(),
            contentDescription = mediaItem.description,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .background(backgroundColor),
            contentScale = ContentScale.FillWidth
        )
    }
}
