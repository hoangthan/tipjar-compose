package com.example.tipjar.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tipjar.database.entity.TipHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface TipHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tipHistory: TipHistory)

    @Query("SELECT * FROM tip_history")
    fun getAll(): Flow<List<TipHistory>>
}
