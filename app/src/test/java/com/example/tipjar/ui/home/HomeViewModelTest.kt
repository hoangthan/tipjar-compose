package com.example.tipjar.ui.home

import app.cash.turbine.test
import com.example.tipjar.common.TestDispatcherProvider
import com.example.tipjar.common.TestViewModelScopeRule
import com.example.tipjar.domain.usecase.CalculatePaymentUseCase
import com.example.tipjar.domain.usecase.SavePaymentUseCase
import com.example.tipjar.ui.home.HomeViewModel.HomeViewEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner.Strict::class)
class HomeViewModelTest {

    @Spy
    private lateinit var dispatcherProvider: TestDispatcherProvider

    @Mock
    private lateinit var savePaymentUseCase: SavePaymentUseCase

    @Spy
    private lateinit var calculatePayment: CalculatePaymentUseCase

    @get:Rule
    val dispatcherRule = TestViewModelScopeRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel(dispatcherProvider, savePaymentUseCase, calculatePayment)
    }

    @Test
    fun testDispatchEvent_takePhoto_success() = runTest {
        val currentState = viewModel.state.value

        viewModel.dispatchEvent(HomeViewEvent.UpdateTakePhoto)
        advanceUntilIdle()

        viewModel.state.test {
            val updateValue = awaitItem()
            assert(updateValue.takePhotoReceipt != currentState.takePhotoReceipt)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDispatchEvent_updateTip_success() = runTest {
        val newTipPercentString = "10"
        viewModel.dispatchEvent(HomeViewEvent.UpdateTipPercent(newTipPercentString))
        advanceUntilIdle()

        viewModel.state.test {
            val updateValue = awaitItem()
            assert(updateValue.tipPercent == newTipPercentString)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDispatchEvent_updateBillAmount_success() = runTest {
        val newAmount = "10"
        viewModel.dispatchEvent(HomeViewEvent.UpdateAmount(newAmount))
        advanceUntilIdle()

        viewModel.state.test {
            val updateValue = awaitItem()
            assert(updateValue.billAmount == newAmount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDispatchEvent_updateNumberOfPeople_success() = runTest {
        val numberOfPeople = 10
        viewModel.dispatchEvent(HomeViewEvent.UpdateNumberOfPeople(numberOfPeople))
        advanceUntilIdle()

        viewModel.state.test {
            val updateValue = awaitItem()
            assert(updateValue.numberOfPeople == numberOfPeople)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
