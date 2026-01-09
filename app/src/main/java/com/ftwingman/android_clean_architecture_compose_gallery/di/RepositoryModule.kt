package com.ftwingman.android_clean_architecture_compose_gallery.di

import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.PhotoRepositoryImpl
import com.ftwingman.android_clean_architecture_compose_gallery.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePhotoRepository(repository: PhotoRepositoryImpl): PhotoRepository {
        return repository
    }
}
