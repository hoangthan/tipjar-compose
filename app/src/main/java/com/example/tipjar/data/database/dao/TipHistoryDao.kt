package com.example.tipjar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tipjar.data.database.entity.TipHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface TipHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tipHistory: TipHistory)

    @Query("SELECT * FROM tip_history WHERE id = :id")
    suspend fun getById(id: Long): TipHistory?

    @Query("SELECT * FROM tip_history")
    fun getAll(): Flow<List<TipHistory>>

    @Query("DELETE FROM tip_history WHERE id = :id")
    suspend fun deleteById(id: Long)
}
