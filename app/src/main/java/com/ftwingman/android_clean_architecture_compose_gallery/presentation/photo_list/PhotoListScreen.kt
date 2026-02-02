package com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.components.PhotoItem

@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel
) {
    val photos = viewModel.photoPagingData.collectAsLazyPagingItems()
    
    PhotoListContent(
        photos = photos
    )
}

@Composable
fun PhotoListContent(
    photos: LazyPagingItems<Photo>,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(photos.itemCount) { index ->
            photos[index]?.let { photo ->
                PhotoItem(photo = photo)
            }
        }
    }
}
