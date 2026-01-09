package com.ftwingman.android_clean_architecture_compose_gallery.di

import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.PhotoRepositoryImpl
import com.ftwingman.android_clean_architecture_compose_gallery.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test
import javax.inject.Singleton

class RepositoryModuleTest {

    @Test
    fun `providePhotoRepository should return PhotoRepositoryImpl`() {
        val impl = mockk<PhotoRepositoryImpl>()
        val repo = RepositoryModule.providePhotoRepository(impl)
        assertTrue(repo is PhotoRepository)
    }
}
