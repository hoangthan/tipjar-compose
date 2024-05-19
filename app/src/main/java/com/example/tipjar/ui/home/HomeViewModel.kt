package com.example.tipjar.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipjar.domain.usecase.CalculatePaymentUseCase
import com.example.tipjar.domain.usecase.SavePaymentUseCase
import com.example.tipjar.ui.common.DispatcherProvider
import com.example.tipjar.ui.utils.asCurrencyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val savePaymentUseCase: SavePaymentUseCase,
    private val calculatePayment: CalculatePaymentUseCase,
) : ViewModel() {

    private val _internalState = MutableStateFlow(HomeViewState())
    private val _state = MutableStateFlow(HomeViewState())
    val state = _state.asStateFlow()

    init {
        listenStateChangeAndCalculate()
    }

    // Since we want to free the main thread from calculation task,
    // by listing the changes of state flow and calculation in Default dispatcher then emit new value to Main Thread display
    private fun listenStateChangeAndCalculate() {
        _internalState
            .onEach { internalState ->
                val param = CalculatePaymentUseCase.CalculatePaymentParam(
                    amount = internalState.billAmount,
                    tipPercent = internalState.tipPercent,
                    numberOfPeople = internalState.numberOfPeople
                )

                val calculatePaymentResult = calculatePayment(param)
                val totalTip = calculatePaymentResult.totalTip.asCurrencyString()
                val perPerson = calculatePaymentResult.costPerPerson.asCurrencyString()

                _state.update {
                    internalState.copy(
                        totalTip = totalTip,
                        perPerson = perPerson
                    )
                }
            }
            .flowOn(dispatcherProvider.default)
            .launchIn(viewModelScope)
    }

    fun dispatchEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.OnSavePaymentClicked -> {
                savePayment()
            }

            is HomeViewEvent.UpdateAmount -> {
                _internalState.update { it.copy(billAmount = event.amount) }
            }

            is HomeViewEvent.UpdateNumberOfPeople -> {
                val numberOfPeople = max(event.amount, 1)
                _internalState.update { it.copy(numberOfPeople = numberOfPeople) }
            }

            is HomeViewEvent.UpdateTakePhoto -> {
                val takePhoto = !state.value.takePhotoReceipt
                _internalState.update { it.copy(takePhotoReceipt = takePhoto) }
            }

            is HomeViewEvent.UpdateTipPercent -> {
                _internalState.update { it.copy(tipPercent = event.percent) }
            }
        }
    }

    private fun savePayment() {
        viewModelScope.launch(dispatcherProvider.io) {
            val param = SavePaymentUseCase.SavePaymentData(
                billAmount = state.value.billAmount.toDouble(),
                tipAmount = state.value.tipPercent.toDouble(),
                billPhoto = state.value.billPhoto
            )
            savePaymentUseCase(param)
            _internalState.update { HomeViewState() }
        }
    }

    sealed interface HomeViewEvent {
        data object UpdateTakePhoto : HomeViewEvent
        data object OnSavePaymentClicked : HomeViewEvent
        data class UpdateAmount(val amount: String) : HomeViewEvent
        data class UpdateNumberOfPeople(val amount: Int) : HomeViewEvent
        data class UpdateTipPercent(val percent: String) : HomeViewEvent
    }

    data class HomeViewState(
        val tipPercent: String = "10",
        val billAmount: String = "",
        val numberOfPeople: Int = 1,
        val takePhotoReceipt: Boolean = false,
        val billPhoto: String? = null,
        val totalTip: String = "",
        val perPerson: String = ""
    )
}
