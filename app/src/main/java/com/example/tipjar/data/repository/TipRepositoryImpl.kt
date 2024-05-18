package com.example.tipjar.data.repository

import com.example.tipjar.data.database.dao.TipHistoryDao
import com.example.tipjar.data.database.entity.toTipHistory
import com.example.tipjar.data.database.entity.toTipModel
import com.example.tipjar.domain.model.TipModel
import com.example.tipjar.domain.repository.TipRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TipRepositoryImpl @Inject constructor(
    private val tipDao: TipHistoryDao,
) : TipRepository {

    override suspend fun getTip(id: Long): TipModel? {
        return tipDao.getById(id)?.toTipModel()
    }

    override suspend fun saveTip(tip: TipModel) {
        val tipEntity = tip.toTipHistory()
        tipDao.insert(tipEntity)
    }

    override fun getAllTip(): Flow<List<TipModel>> {
        return tipDao.getAll().map { tips ->
            tips.map { it.toTipModel() }
        }
    }
}
