package com.vau.studio.iosstyle.idialer_phone.data

import com.vau.studio.iosstyle.idialer_phone.data.local.SharePreferenceClient
import com.vau.studio.iosstyle.idialer_phone.data.repositories.PhoneRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Provides
    fun provideSharePreferenceClient(): SharePreferenceClient {
        return SharePreferenceClient
    }

    @Provides
    fun providePhoneRepository() : PhoneRepository {
        return PhoneRepository
    }
}