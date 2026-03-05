package com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaItem

/**
 * Pexels 圖片項目的精簡縮圖卡片（不含 Shared Transition）。
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

    ElevatedCard(
        onClick = onItemClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(mediaItem.thumbnailUrl)
                .crossfade(true)
                .build(),
            contentDescription = mediaItem.description,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            contentScale = ContentScale.FillWidth
        )
    }
}
