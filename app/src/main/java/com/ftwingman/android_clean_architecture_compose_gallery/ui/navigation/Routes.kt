package com.ftwingman.android_clean_architecture_compose_gallery.ui.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object Menu : Route()

    @Serializable
    data object PhotoList : Route()

    @Serializable
    data class PhotoDetail(val photoId: String, val thumbnailUrl: String) : Route()

    /**
     * 媒體列表頁（Pexels）。
     * @param mode "IMAGE"、"VIDEO" 或 "MIXED"
     */
    @Serializable
    data class MediaList(val mode: String) : Route()

    @Serializable
    data class MediaDetail(val mediaId: String, val thumbnailUrl: String) : Route()
}
