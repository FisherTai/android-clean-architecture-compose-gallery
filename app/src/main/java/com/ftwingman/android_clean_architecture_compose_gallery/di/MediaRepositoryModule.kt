package com.ftwingman.android_clean_architecture_compose_gallery.di

import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.MediaRepositoryImpl
import com.ftwingman.android_clean_architecture_compose_gallery.domain.repository.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository
}
