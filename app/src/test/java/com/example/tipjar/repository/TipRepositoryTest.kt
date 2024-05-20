package com.example.tipjar.repository

import com.example.tipjar.data.database.dao.TipHistoryDao
import com.example.tipjar.data.database.entity.TipHistory
import com.example.tipjar.data.repository.TipRepositoryImpl
import com.example.tipjar.domain.model.TipModel
import com.example.tipjar.domain.repository.TipRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
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
        val record = tipRepository.getById(1)
        Assert.assertNotNull(record)
        Assert.assertEquals(record, TipModel(1, 10.0, 1.0, "img", 1))
    }

    @Test
    fun getAllTip_success() = runTest {
        val givenRecords = listOf(
            TipHistory(1, 10.0, 1.0, "img1"),
            TipHistory(2, 20.0, 2.0, "img2"),
        )
        whenever(tipDao.getAll()).thenReturn(flowOf(givenRecords))

        val records = tipDao.getAll().first()
        Assert.assertEquals(givenRecords, records)
    }

    @Test
    fun deleteById_success() = runTest {
        whenever(tipDao.deleteById(any())).thenReturn(Unit)
        val result = tipRepository.deleteById(1)
        Assert.assertTrue(result == Unit)
    }
}
