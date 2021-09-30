package com.vau.studio.iosstyle.idialer_phone.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
internal object DispatcherModule {

    @Provides
    fun provideIoDispatcher() : CoroutineDispatcher{
        return Dispatchers.IO
    }

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}