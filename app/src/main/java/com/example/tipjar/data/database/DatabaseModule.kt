package com.example.tipjar.data.database

import android.content.Context
import com.example.tipjar.data.database.dao.TipHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesTipDatabase(
        @ApplicationContext context: Context
    ): TipDatabase {
        return TipDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideTipHistoryDao(tipDatabase: TipDatabase): TipHistoryDao {
        return tipDatabase.tipHistoryDao()
    }
}
