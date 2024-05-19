package com.example.tipjar.repository

import com.example.tipjar.data.database.dao.TipHistoryDao
import com.example.tipjar.data.database.entity.TipHistory
import com.example.tipjar.data.repository.TipRepositoryImpl
import com.example.tipjar.domain.model.TipModel
import com.example.tipjar.domain.repository.TipRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner.Strict::class)
class TipRepositoryTest {

    @Mock
    private lateinit var tipDao: TipHistoryDao

    private lateinit var tipRepository: TipRepository

    @Before
    fun setup() {
        tipRepository = TipRepositoryImpl(tipDao)
    }

    @Test
    fun getTip_success() = runTest {
        whenever(tipDao.getById(1)).thenReturn(TipHistory(1, 10.0, 1.0, "img"))
        val record = tipRepository.getTip(1)
        Assert.assertNotNull(record)
        Assert.assertEquals(record, TipModel(1, 10.0, 1.0, "img"))
    }
}
