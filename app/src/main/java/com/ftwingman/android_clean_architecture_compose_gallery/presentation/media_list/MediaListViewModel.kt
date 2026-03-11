@file:OptIn(androidx.media3.common.util.UnstableApi::class)

package com.ftwingman.android_clean_architecture_compose_gallery.presentation.media_list

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem as Media3Item
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaItem
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaType
import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.MediaRepository
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 媒體列表 ViewModel。
 *
 * 維護全域唯一的 ExoPlayer 實例，依滾動偵測結果切換播放目標。
 */
@HiltViewModel
class MediaListViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val cacheDataSourceFactory: CacheDataSource.Factory,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route: Route.MediaList = savedStateHandle.toRoute()

    private val mediaType: MediaType? = when (route.mode) {
        "IMAGE" -> MediaType.IMAGE
        "VIDEO" -> MediaType.VIDEO
        else -> null // MIXED
    }

    val mediaPagingData = mediaRepository
        .getMediaItems(mediaType)
        .cachedIn(viewModelScope)

    /** 全域唯一播放器實例，配置共用快取以達成零流量重播 */
    val player: ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
        .build()
        .apply {
            volume = 0f // 預設靜音預覽
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
        }

    /** 當前正在播放的 MediaItem id */
    private val _playingItemId = MutableStateFlow<String?>(null)
    val playingItemId: StateFlow<String?> = _playingItemId.asStateFlow()

    /**
     * 由 UI 呼叫，當滾動位置改變後，傳入最靠近畫面中心的影片 Item。
     * 若與目前播放項目相同則不重新載入。
     */
    fun onCenterVideoChanged(mediaItem: MediaItem?) {
        if (mediaItem == null) {
            player.pause()
            _playingItemId.value = null
            return
        }

        if (mediaItem.id == _playingItemId.value) return

        val videoUrl = mediaItem.videoUrl
        if (videoUrl == null) {
            player.pause()
            _playingItemId.value = null
            return
        }

        _playingItemId.value = mediaItem.id
        player.setMediaItem(Media3Item.fromUri(videoUrl))
        player.prepare()
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
