package com.example.tipjar.ui.history

import app.cash.turbine.test
import com.example.tipjar.common.TestViewModelScopeRule
import com.example.tipjar.domain.model.TipModel
import com.example.tipjar.domain.usecase.DeleteTipRecordUseCase
import com.example.tipjar.domain.usecase.GetAllTipHistoryUseCase
import com.example.tipjar.ui.common.AppDispatcherProvider
import com.example.tipjar.ui.history.HistoryViewModel.ViewEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner.Strict::class)
class HistoryViewModelTest {

    private lateinit var historyViewModel: HistoryViewModel

    @Mock
    lateinit var loadHistoryUseCase: GetAllTipHistoryUseCase

    @Mock
    private lateinit var deletePaymentUseCase: DeleteTipRecordUseCase

    @Spy
    private lateinit var dispatcherProvider: AppDispatcherProvider

    @get:Rule
    val dispatcherRule = TestViewModelScopeRule()

    @Test
    fun initViewModel_and_loadData_success() = runTest {
        val fakeRecords = listOf(
            TipModel(id = 1L, billAmount = 10.0, tipAmount = 1.0, imageUrl = "img"),
            TipModel(id = 2L, billAmount = 10.0, tipAmount = 1.0, imageUrl = "img")
        )

        whenever(loadHistoryUseCase()).thenReturn(flowOf(fakeRecords))

        historyViewModel = HistoryViewModel(
            loadHistoryUseCase = loadHistoryUseCase,
            deletePaymentUseCase = deletePaymentUseCase,
            dispatcherProvider = dispatcherProvider,
        )

        advanceUntilIdle()

        historyViewModel.viewState.test {
            val expectedData = awaitItem().payments
            assert(expectedData.size == fakeRecords.size)
            assert(expectedData.map { it.id } == fakeRecords.map { it.id })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDelete_success() = runTest {
        val fakeRecords = listOf(
            TipModel(id = 1L, billAmount = 10.0, tipAmount = 1.0, imageUrl = "img"),
            TipModel(id = 2L, billAmount = 10.0, tipAmount = 1.0, imageUrl = "img")
        )

        val flowData = MutableStateFlow(fakeRecords)
        whenever(loadHistoryUseCase()).thenReturn(flowData)

        historyViewModel = HistoryViewModel(
            loadHistoryUseCase = loadHistoryUseCase,
            deletePaymentUseCase = deletePaymentUseCase,
            dispatcherProvider = dispatcherProvider,
        )

        advanceUntilIdle()

        historyViewModel.dispatchViewEvent(ShowBillDetails(1))
        flowData.update { it.filterNot { it.id == 1L } }

        historyViewModel.viewState.test {
            val expectData = awaitItem()
            assert(expectData.payments.size == fakeRecords.size - 1)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
