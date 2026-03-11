package com.ftwingman.android_clean_architecture_compose_gallery.model

/**
 * 代表 Unsplash 用戶（攝影師）的領域模型。
 */
data class User(
    val id: String,
    val username: String,
    val name: String,
    val profileImage: String
)
