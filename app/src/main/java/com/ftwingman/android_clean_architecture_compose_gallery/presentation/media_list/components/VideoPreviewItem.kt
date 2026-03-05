@file:OptIn(androidx.media3.common.util.UnstableApi::class)

package com.ftwingman.android_clean_architecture_compose_gallery.presentation.media_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaItem

/**
 * 影片預覽卡片元件。
 *
 * - 靜止狀態：顯示影片首圖（thumbnailUrl）+ 播放圖示
 * - 播放中：顯示 ExoPlayer PlayerView（由外部傳入的 player 實例控制）
 *
 * @param player 非 null 時表示此卡片為當前播放中項目，掛載 ExoPlayer PlayerView
 */
@Composable
fun VideoPreviewItem(
    mediaItem: MediaItem,
    player: ExoPlayer?,
    isPlaying: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val aspectRatio = if (mediaItem.width > 0 && mediaItem.height > 0) {
        mediaItem.width.toFloat() / mediaItem.height.toFloat()
    } else {
        9f / 16f
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .clickable { onItemClick() }
        ) {
            if (isPlaying && player != null) {
                // 播放中：渲染 PlayerView
                VideoPlayerView(
                    player = player,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // 靜止：顯示縮圖 + 播放按鈕
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(mediaItem.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = mediaItem.description,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                // 播放圖示
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "播放影片",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

/**
 * 包裝 Media3 PlayerView 的 AndroidView 橋接元件。
 * 使用 AndroidView 的 update/onRelease 正確管理 PlayerView 生命週期，
 * 避免因 player 參考變更導致頻繁 dispose/recreate。
 *
 * @param useController 是否顯示播放控制列（列表預覽 false，詳情頁 true）
 */
@Composable
fun VideoPlayerView(
    player: ExoPlayer,
    modifier: Modifier = Modifier,
    useController: Boolean = false
) {
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                this.useController = useController
            }
        },
        update = { view -> view.player = player },
        onRelease = { view -> view.player = null },
        modifier = modifier
    )
}
