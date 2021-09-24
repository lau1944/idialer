package com.vau.studio.iosstyle.idialer_phone.data

import com.vau.studio.iosstyle.idialer_phone.data.local.AppDatabase
import com.vau.studio.iosstyle.idialer_phone.data.local.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase) : FavoriteDao {
        return appDatabase.favoriteDao()
    }

}