package com.example.tipjar.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.tipjar.data.database.TipDatabase
import com.example.tipjar.data.database.dao.TipHistoryDao
import com.example.tipjar.data.database.entity.TipHistory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TipDaoTest {

    private lateinit var tipDatabase: TipDatabase
    private lateinit var tipDao: TipHistoryDao

    @Before
    fun setup() {
        tipDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TipDatabase::class.java
        ).build()

        tipDao = tipDatabase.tipHistoryDao()
    }

    @Test
    fun saveTip_success() = runTest {
        val insertRecords = listOf(
            TipHistory(1, 1.0, 1.0, "img"),
            TipHistory(2, 1.0, 1.0, "img")
        )
        tipDao.insert(*insertRecords.toTypedArray())

        val allRecords = tipDao.getAll().first()
        Assert.assertEquals(allRecords, insertRecords)
        Assert.assertTrue(allRecords.map { it.id }.containsAll(listOf(1, 2)))
    }

    @Test
    fun deleteTipById_success() = runTest {
        val insertRecords = listOf(
            TipHistory(1, 1.0, 1.0, "img"),
            TipHistory(2, 1.0, 1.0, "img")
        )
        tipDao.insert(*insertRecords.toTypedArray())
        tipDao.deleteById(2)

        val allRecords = tipDao.getAll().first()
        Assert.assertTrue(allRecords.size == 1)
        Assert.assertTrue(allRecords.first().id == 1L)
    }

    @Test
    fun saveTip_replace_same_key() = runTest {
        val insertRecords = listOf(
            TipHistory(1, 1.0, 1.0, "img"),
            TipHistory(1, 1.0, 1.0, "img")
        )
        tipDao.insert(*insertRecords.toTypedArray())

        val allRecords = tipDao.getAll().first()
        assert(allRecords.size == 1)
        assert(allRecords.first().id == 1L)
    }

    @Test
    fun getAll_success_after_save() = runTest {
        val flowData = tipDao.getAll()

        val insertRecord = TipHistory(1, 1.0, 1.0, "img")
        tipDao.insert(insertRecord)

        flowData.test {
            val records = awaitItem()
            assert(records.size == 1)
            assert(records.first() == insertRecord)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        tipDatabase.close()
    }
}
