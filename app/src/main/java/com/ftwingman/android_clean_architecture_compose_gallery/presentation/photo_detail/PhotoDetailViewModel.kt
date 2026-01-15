package com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val repository: PhotoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val photoId: String = checkNotNull(savedStateHandle["photoId"])
    val thumbnailUrl: String = checkNotNull(savedStateHandle["thumbnailUrl"])

    val photo: StateFlow<Photo?> = repository.getPhotoById(photoId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        // Trigger background refresh to get EXIF and other full details
        viewModelScope.launch {
            repository.refreshPhotoDetail(photoId)
        }
    }
}