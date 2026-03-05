@file:OptIn(androidx.media3.common.util.UnstableApi::class)

package com.ftwingman.android_clean_architecture_compose_gallery.di

import android.content.Context
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.database.StandaloneDatabaseProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

/**
 * 提供影片快取相關的 Singleton 依賴。
 * - SimpleCache：全域唯一實例，確保 LRU 淘汰策略正確運作。
 * - CacheDataSource.Factory：供所有 ExoPlayer 實例共用，達成「零流量秒開」效果。
 */
@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    private const val MAX_CACHE_SIZE = 200L * 1024 * 1024 // 200 MB

    @Provides
    @Singleton
    fun provideVideoCache(@ApplicationContext context: Context): SimpleCache {
        val cacheDir = File(context.cacheDir, "video_cache")
        val databaseProvider = StandaloneDatabaseProvider(context)
        return SimpleCache(
            cacheDir,
            LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE),
            databaseProvider
        )
    }

    @Provides
    @Singleton
    fun provideCacheDataSourceFactory(
        @ApplicationContext context: Context,
        videoCache: SimpleCache
    ): CacheDataSource.Factory {
        val upstreamFactory = DefaultDataSource.Factory(context)
        return CacheDataSource.Factory()
            .setCache(videoCache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
}
