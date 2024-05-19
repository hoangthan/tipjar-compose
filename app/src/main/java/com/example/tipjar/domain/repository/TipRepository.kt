package com.example.tipjar.domain.repository

import com.example.tipjar.domain.model.TipModel
import kotlinx.coroutines.flow.Flow

interface TipRepository {

    suspend fun getById(id: Long): TipModel?

    suspend fun save(billAmount: Double, tipAmount: Double, imageUrl: String?)

    suspend fun deleteById(id: Long)

    fun getAll(): Flow<List<TipModel>>
}
