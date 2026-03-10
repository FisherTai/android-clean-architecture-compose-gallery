package com.ftwingman.android_clean_architecture_compose_gallery.presentation.media_list.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaItem

/**
 * 圖影卡片共用外殼。
 *
 * 提供統一的視覺與互動體驗：
 * - 按壓時的 scale 縮放動畫
 * - avgColor 佔位底色
 * - 攝影師首字母圓形 Badge + 名稱 + 描述的 Metadata Section
 *
 * @param mediaContent 媒體內容插槽（圖片 AsyncImage 或影片 PlayerView）
 */
@Composable
fun MediaCardWrapper(
    mediaItem: MediaItem,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    mediaContent: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "PressScale"
    )

    ElevatedCard(
        onClick = onItemClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = MaterialTheme.shapes.medium,
        interactionSource = interactionSource
    ) {
        Column {
            mediaContent()

            // Metadata Section
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PhotographerAvatar(name = mediaItem.photographer)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mediaItem.photographer,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                mediaItem.description?.let { desc ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * 攝影師首字母圓形 Badge。
 * 從名字取第一個字元，顯示在帶有主題色背景的圓形中。
 */
@Composable
private fun PhotographerAvatar(name: String) {
    val initial = name.firstOrNull()?.uppercase() ?: "?"
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

/**
 * 解析 avgColor 十六進制字串為 Compose Color，失敗時回傳透明。
 */
fun parseAvgColor(hex: String?): Color {
    return try {
        hex?.let { Color(android.graphics.Color.parseColor(it)) } ?: Color.Transparent
    } catch (e: Exception) {
        Color.Transparent
    }
}
