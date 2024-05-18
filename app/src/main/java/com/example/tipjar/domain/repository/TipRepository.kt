package com.example.tipjar.domain.repository

import com.example.tipjar.domain.model.TipModel
import kotlinx.coroutines.flow.Flow

interface TipRepository {

    suspend fun getTip(id: Long): TipModel?

    suspend fun saveTip(tip: TipModel)

    fun getAllTip(): Flow<List<TipModel>>
}
