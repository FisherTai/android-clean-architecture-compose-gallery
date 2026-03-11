@file:OptIn(androidx.media3.common.util.UnstableApi::class)

package com.ftwingman.android_clean_architecture_compose_gallery.ui.media_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ftwingman.android_clean_architecture_compose_gallery.model.MediaItem
import com.ftwingman.android_clean_architecture_compose_gallery.model.MediaType
import com.ftwingman.android_clean_architecture_compose_gallery.ui.media_list.components.VideoPlayerView

@Composable
fun MediaDetailScreen(
    viewModel: MediaDetailViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mediaItem by viewModel.mediaItem.collectAsState()
    val isLoaded by viewModel.isLoaded.collectAsState()

    // 生命週期連動：App 退到背景時暫停播放，回到前景時恢復
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, viewModel) {
        var wasPlaying = false
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    wasPlaying = viewModel.player.playWhenReady
                    viewModel.player.pause()
                }
                Lifecycle.Event.ON_START -> {
                    if (wasPlaying) viewModel.player.play()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                mediaItem != null -> {
                    MediaDetailContent(
                        mediaItem = mediaItem!!,
                        player = viewModel.player,
                        thumbnailUrl = viewModel.thumbnailUrl
                    )
                }
                isLoaded -> {
                    // 資料載入完成但找不到項目
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "找不到此媒體項目",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.TextButton(onClick = onBackClick) {
                            Text(text = "返回")
                        }
                    }
                }
                else -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            // 浮動返回按鈕
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Surface(
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.3f),
                    contentColor = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaDetailContent(
    mediaItem: MediaItem,
    player: androidx.media3.exoplayer.ExoPlayer,
    thumbnailUrl: String,
    modifier: Modifier = Modifier
) {
    val aspectRatio = if (mediaItem.width > 0 && mediaItem.height > 0) {
        mediaItem.width.toFloat() / mediaItem.height.toFloat()
    } else 16f / 9f

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        when (mediaItem.mediaType) {
            MediaType.VIDEO -> {
                DetailPlayerView(
                    player = player,
                    thumbnailUrl = thumbnailUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                )
            }
            MediaType.IMAGE -> {
                AsyncImage(
                    model = mediaItem.imageUrl ?: mediaItem.thumbnailUrl,
                    contentDescription = mediaItem.description,
                    placeholder = coil.compose.rememberAsyncImagePainter(thumbnailUrl),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio),
                    contentScale = ContentScale.FillWidth
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            mediaItem.description?.let { desc ->
                Text(
                    text = desc,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "攝影師",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mediaItem.photographer,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "尺寸：${mediaItem.width} x ${mediaItem.height}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * 詳情頁 PlayerView，啟用播放控制列。
 * 複用 VideoPlayerView 共用元件，帶縮圖過渡避免黑畫面。
 */
@Composable
private fun DetailPlayerView(
    player: androidx.media3.exoplayer.ExoPlayer,
    thumbnailUrl: String,
    modifier: Modifier = Modifier
) {
    VideoPlayerView(
        player = player,
        modifier = modifier,
        useController = true,
        thumbnailUrl = thumbnailUrl
    )
}
