package com.vau.studio.iosstyle.idialer_phone.data

import android.content.Context
import androidx.room.Room
import com.vau.studio.iosstyle.idialer_phone.data.local.AppDatabase
import com.vau.studio.iosstyle.idialer_phone.data.local.FavoriteDao
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
    fun provideFavoriteDao(appDatabase: AppDatabase) : FavoriteDao {
        return appDatabase.favoriteDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }

}