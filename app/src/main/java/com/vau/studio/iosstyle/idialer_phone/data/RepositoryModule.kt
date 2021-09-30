package com.vau.studio.iosstyle.idialer_phone.data

import com.vau.studio.iosstyle.idialer_phone.data.local.FavoriteDao
import com.vau.studio.iosstyle.idialer_phone.data.local.SharePreferenceClient
import com.vau.studio.iosstyle.idialer_phone.data.repositories.FavoriteRepository
import com.vau.studio.iosstyle.idialer_phone.data.repositories.PhoneRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Provides
    fun provideFavoriteRepository(
        favoriteDao: FavoriteDao,
        dispatcher: CoroutineDispatcher = DispatcherModule.provideIoDispatcher()
    ): FavoriteRepository {
        return FavoriteRepository(dispatcher, favoriteDao)
    }

    @Provides
    fun provideSharePreferenceClient(): SharePreferenceClient {
        return SharePreferenceClient
    }

    @Provides
    fun providePhoneRepository() : PhoneRepository {
        return PhoneRepository
    }
}