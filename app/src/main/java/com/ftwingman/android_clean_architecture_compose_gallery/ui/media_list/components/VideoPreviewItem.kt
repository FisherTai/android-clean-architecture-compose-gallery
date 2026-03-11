@file:OptIn(androidx.media3.common.util.UnstableApi::class)

package com.ftwingman.android_clean_architecture_compose_gallery.ui.media_list.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ftwingman.android_clean_architecture_compose_gallery.model.MediaItem

/**
 * 影片預覽卡片元件。
 *
 * 使用 [MediaCardWrapper] 共用外殼，提供與圖片卡片一致的視覺與互動體驗。
 * - 靜止狀態：顯示影片首圖（thumbnailUrl）+ 右上角影片角標
 * - 播放中：顯示 ExoPlayer PlayerView（由外部傳入的 player 實例控制）
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

    val backgroundColor = parseAvgColor(mediaItem.avgColor)

    MediaCardWrapper(
        mediaItem = mediaItem,
        onItemClick = onItemClick,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .background(backgroundColor)
        ) {
            if (isPlaying && player != null) {
                VideoPlayerView(
                    player = player,
                    modifier = Modifier.fillMaxSize(),
                    thumbnailUrl = mediaItem.thumbnailUrl
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(mediaItem.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = mediaItem.description,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }

            // 影片角標（右上角小圖示）
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "影片",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * 包裝 Media3 PlayerView 的 AndroidView 橋接元件。
 * 使用 AndroidView 的 update/onRelease 正確管理 PlayerView 生命週期。
 *
 * 當提供 [thumbnailUrl] 時，會在影片第一幀渲染完成前顯示縮圖覆蓋層，
 * 渲染完成後以淡出動畫平滑過渡，避免黑畫面閃爍。
 *
 * @param useController 是否顯示播放控制列（列表預覽 false，詳情頁 true）
 * @param thumbnailUrl 過渡用縮圖 URL，null 則不顯示縮圖覆蓋層
 */
@Composable
fun VideoPlayerView(
    player: ExoPlayer,
    modifier: Modifier = Modifier,
    useController: Boolean = false,
    thumbnailUrl: String? = null
) {
    var hasRenderedFirstFrame by remember { mutableStateOf(false) }

    // 監聽第一幀渲染與 media item 切換
    DisposableEffect(player) {
        hasRenderedFirstFrame = false
        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                hasRenderedFirstFrame = true
            }

            override fun onMediaItemTransition(
                mediaItem: androidx.media3.common.MediaItem?,
                reason: Int
            ) {
                hasRenderedFirstFrame = false
            }
        }
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx -> PlayerView(ctx) },
            update = { view ->
                view.player = player
                view.useController = useController
            },
            onRelease = { view -> view.player = null },
            modifier = Modifier.fillMaxSize()
        )

        // 縮圖覆蓋層：第一幀渲染前顯示，渲染後淡出
        if (thumbnailUrl != null) {
            AnimatedVisibility(
                visible = !hasRenderedFirstFrame,
                exit = fadeOut(animationSpec = tween(300))
            ) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}
