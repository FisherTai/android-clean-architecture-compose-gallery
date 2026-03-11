@file:OptIn(androidx.media3.common.util.UnstableApi::class)

package com.ftwingman.android_clean_architecture_compose_gallery.presentation.media_detail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem as Media3Item
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.navigation.toRoute
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaItem
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaType
import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.MediaRepository
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 媒體詳情 ViewModel。
 *
 * 維護獨立的 ExoPlayer 實例（與列表頁互不干擾），
 * 但共用相同的 CacheDataSource.Factory 以達成快取命中。
 */
@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val cacheDataSourceFactory: CacheDataSource.Factory,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route: Route.MediaDetail = savedStateHandle.toRoute()
    val thumbnailUrl: String = route.thumbnailUrl

    private val _mediaItem = MutableStateFlow<MediaItem?>(null)
    val mediaItem: StateFlow<MediaItem?> = _mediaItem.asStateFlow()

    /** 標記資料載入是否完成（區分 loading 與 not-found） */
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()

    /** 詳情頁獨立播放器，使用共用快取 */
    val player: ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
        .build()

    init {
        viewModelScope.launch {
            val item = mediaRepository.getMediaItemById(route.mediaId)
            _mediaItem.value = item
            _isLoaded.value = true
            if (item?.mediaType == MediaType.VIDEO) {
                item.videoUrl?.let { url ->
                    player.setMediaItem(Media3Item.fromUri(url))
                    player.prepare()
                    player.playWhenReady = true
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
