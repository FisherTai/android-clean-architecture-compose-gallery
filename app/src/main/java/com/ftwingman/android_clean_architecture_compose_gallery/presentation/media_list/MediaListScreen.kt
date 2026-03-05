package com.ftwingman.android_clean_architecture_compose_gallery.presentation.media_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaItem
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.MediaType
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.media_list.components.VideoPreviewItem
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.ErrorRetryIndicator
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.LoadingIndicator
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.components.MediaThumbnailItem
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListScreen(
    viewModel: MediaListViewModel,
    onBackClick: () -> Unit,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val mediaItems = viewModel.mediaPagingData.collectAsLazyPagingItems()
    val playingItemId by viewModel.playingItemId.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val gridState = rememberLazyStaggeredGridState()

    // 離開列表頁時暫停播放器（例如導航到詳情頁）
    DisposableEffect(viewModel) {
        onDispose { viewModel.onCenterVideoChanged(null) }
    }

    // 滾動偵測：找出最靠近畫面中心的影片 Item（加 debounce 避免快速滑動時頻繁切換）
    @OptIn(FlowPreview::class)
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo }
            .distinctUntilChanged()
            .debounce(300L)
            .collect { layoutInfo ->
                val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                val centerVideoItem = layoutInfo.visibleItemsInfo
                    .minByOrNull { itemInfo ->
                        val itemCenter = (itemInfo.offset.y + itemInfo.size.height / 2)
                        kotlin.math.abs(itemCenter - viewportCenter)
                    }
                    ?.let { itemInfo ->
                        val index = itemInfo.index
                        if (index < mediaItems.itemCount) mediaItems[index] else null
                    }
                    ?.takeIf { it.mediaType == MediaType.VIDEO }

                viewModel.onCenterVideoChanged(centerVideoItem)
            }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("影音瀏覽", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        MediaListContent(
            mediaItems = mediaItems,
            playingItemId = playingItemId,
            player = viewModel.player,
            onMediaClick = onMediaClick,
            gridState = gridState,
            contentPadding = innerPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaListContent(
    mediaItems: LazyPagingItems<MediaItem>,
    playingItemId: String?,
    player: androidx.media3.exoplayer.ExoPlayer,
    onMediaClick: (MediaItem) -> Unit,
    gridState: androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullToRefreshState()
    val isRefreshing = mediaItems.loadState.refresh is LoadState.Loading && mediaItems.itemCount > 0

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { mediaItems.refresh() },
        state = pullRefreshState,
        modifier = modifier
            .fillMaxSize()
            .padding(top = contentPadding.calculateTopPadding())
    ) {
        LazyVerticalStaggeredGrid(
            state = gridState,
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = contentPadding.calculateBottomPadding() + 8.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp
        ) {
            items(
                count = mediaItems.itemCount,
                key = { index -> mediaItems.peek(index)?.id ?: index }
            ) { index ->
                mediaItems[index]?.let { item ->
                    when (item.mediaType) {
                        MediaType.VIDEO -> {
                            VideoPreviewItem(
                                mediaItem = item,
                                player = if (playingItemId == item.id) player else null,
                                isPlaying = playingItemId == item.id,
                                onItemClick = { onMediaClick(item) }
                            )
                        }
                        MediaType.IMAGE -> {
                            MediaThumbnailItem(
                                mediaItem = item,
                                onItemClick = { onMediaClick(item) }
                            )
                        }
                    }
                }
            }

            when (val state = mediaItems.loadState.append) {
                is LoadState.Loading -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        LoadingIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
                is LoadState.Error -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        ErrorRetryIndicator(
                            message = state.error.localizedMessage ?: "載入失敗",
                            onRetry = { mediaItems.retry() }
                        )
                    }
                }
                else -> {}
            }
        }

        if (mediaItems.loadState.refresh is LoadState.Loading && mediaItems.itemCount == 0) {
            LoadingIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (mediaItems.loadState.refresh is LoadState.Error) {
            val state = mediaItems.loadState.refresh as LoadState.Error
            ErrorRetryIndicator(
                message = state.error.localizedMessage ?: "載入失敗",
                onRetry = { mediaItems.refresh() },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
