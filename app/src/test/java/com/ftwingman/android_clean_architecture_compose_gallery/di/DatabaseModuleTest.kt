package com.ftwingman.android_clean_architecture_compose_gallery.di

import android.content.Context
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.InfiniteMuseDatabase
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class DatabaseModuleTest {

    private val context = mockk<Context>(relaxed = true)

    @Test
    fun `provideDatabase should return non-null database`() {
        val db = DatabaseModule.provideDatabase(context)
        assertNotNull(db)
    }

    @Test
    fun `providePhotoDao should return non-null dao`() {
        val db = mockk<InfiniteMuseDatabase>()
        io.mockk.every { db.photoDao() } returns mockk()
        
        val dao = DatabaseModule.providePhotoDao(db)
        assertNotNull(dao)
    }

    @Test
    fun `provideRemoteKeysDao should return non-null dao`() {
        val db = mockk<InfiniteMuseDatabase>()
        io.mockk.every { db.remoteKeysDao() } returns mockk()
        
        val dao = DatabaseModule.provideRemoteKeysDao(db)
        assertNotNull(dao)
    }
}
