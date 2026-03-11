package com.ftwingman.android_clean_architecture_compose_gallery.di

import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.PhotoRepositoryImpl
import com.ftwingman.android_clean_architecture_compose_gallery.data.repository.PhotoRepository
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

class RepositoryModuleTest {

    @Test
    fun `providePhotoRepository should return PhotoRepositoryImpl`() {
        val impl = mockk<PhotoRepositoryImpl>()
        val repo = RepositoryModule.providePhotoRepository(impl)
        assertTrue(repo is PhotoRepository)
    }
}
