package com.example.tipjar.data.repository

import com.example.tipjar.data.database.dao.TipHistoryDao
import com.example.tipjar.data.database.entity.TipHistory
import com.example.tipjar.data.database.entity.toTipModel
import com.example.tipjar.domain.model.TipModel
import com.example.tipjar.domain.repository.TipRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TipRepositoryImpl @Inject constructor(
    private val tipDao: TipHistoryDao,
) : TipRepository {

    override suspend fun getById(id: Long): TipModel? {
        return tipDao.getById(id)?.toTipModel()
    }

    override suspend fun save(billAmount: Double, tipAmount: Double, imageUrl: String?) {
        val tipEntity = TipHistory(
            billAmount = billAmount,
            tipAmount = tipAmount,
            imageUrl = imageUrl
        )

        tipDao.insert(tipEntity)
    }

    override suspend fun deleteById(id: Long) {
        tipDao.deleteById(id)
    }

    override fun getAll(): Flow<List<TipModel>> {
        return tipDao.getAll().map { tips ->
            tips.map { it.toTipModel() }
        }
    }
}
