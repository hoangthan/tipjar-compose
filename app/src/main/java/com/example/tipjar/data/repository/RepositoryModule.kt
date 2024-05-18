package com.example.tipjar.data.repository

import com.example.tipjar.domain.repository.TipRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindTipRepository(impl: TipRepositoryImpl): TipRepository
}
