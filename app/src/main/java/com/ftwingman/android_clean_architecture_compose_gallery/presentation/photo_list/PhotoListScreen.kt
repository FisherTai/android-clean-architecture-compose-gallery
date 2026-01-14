package com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ftwingman.android_clean_architecture_compose_gallery.R
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.components.PhotoItem

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel,
    onPhotoClick: (Photo) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    val photos = viewModel.photoPagingData.collectAsLazyPagingItems()
    
    with(sharedTransitionScope) {
        PhotoListContent(
            photos = photos,
            onPhotoClick = onPhotoClick,
            animatedVisibilityScope = animatedVisibilityScope
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PhotoListContent(
    photos: LazyPagingItems<Photo>,
    onPhotoClick: (Photo) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp
        ) {
            items(photos.itemCount) { index ->
                photos[index]?.let { photo ->
                    PhotoItem(
                        photo = photo,
                        onPhotoClick = { onPhotoClick(photo) },
                        animatedVisibilityScope = animatedVisibilityScope,
                        sharedTransitionScope = this@PhotoListContent
                    )
                }
            }

            // Handle Append (Load More) states
            when (val state = photos.loadState.append) {
                is LoadState.Loading -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        LoadingIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
                is LoadState.Error -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        ErrorRetryIndicator(
                            message = state.error.localizedMessage ?: "Unknown Error",
                            onRetry = { photos.retry() }
                        )
                    }
                }
                else -> {}
            }
        }

        // Handle Refresh (Initial Load) states
        when (val state = photos.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is LoadState.Error -> {
                ErrorRetryIndicator(
                    message = state.error.localizedMessage ?: "Unknown Error",
                    onRetry = { photos.retry() },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {}
        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorRetryIndicator(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 8.dp)) {
            Text(text = "Retry")
        }
    }
}
