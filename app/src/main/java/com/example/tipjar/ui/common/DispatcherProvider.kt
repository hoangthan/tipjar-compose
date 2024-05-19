package com.example.tipjar.ui.common

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}

class AppDispatcherProvider @Inject constructor() : DispatcherProvider {
    override val io = Dispatchers.IO
    override val main = Dispatchers.Main
    override val default = Dispatchers.Default
}

@Module
@InstallIn(SingletonComponent::class)
interface DispatcherProviderModule {

    @Singleton
    @Binds
    fun provideAppDispatcher(impl: AppDispatcherProvider): DispatcherProvider
}
