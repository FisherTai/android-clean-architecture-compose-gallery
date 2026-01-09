package com.ftwingman.android_clean_architecture_compose_gallery.di

import android.content.Context
import androidx.room.Room
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.InfiniteMuseDatabase
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.PhotoDao
import com.ftwingman.android_clean_architecture_compose_gallery.data.local.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): InfiniteMuseDatabase {
        return Room.databaseBuilder(
            context,
            InfiniteMuseDatabase::class.java,
            "infinite_muse.db"
        ).build()
    }

    @Provides
    fun providePhotoDao(database: InfiniteMuseDatabase): PhotoDao {
        return database.photoDao()
    }

    @Provides
    fun provideRemoteKeysDao(database: InfiniteMuseDatabase): RemoteKeysDao {
        return database.remoteKeysDao()
    }
}
