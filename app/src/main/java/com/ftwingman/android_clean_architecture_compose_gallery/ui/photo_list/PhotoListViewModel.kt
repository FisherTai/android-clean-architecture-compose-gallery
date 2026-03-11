package com.ftwingman.android_clean_architecture_compose_gallery.ui.photo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ftwingman.android_clean_architecture_compose_gallery.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {

    val photoPagingData: Flow<PagingData<Photo>> = repository
        .getPhotos()
        .cachedIn(viewModelScope)
}
