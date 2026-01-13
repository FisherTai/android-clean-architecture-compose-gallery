package com.ftwingman.android_clean_architecture_compose_gallery.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object PhotoList : Route()

    @Serializable
    data class PhotoDetail(val photoId: String) : Route()
}
