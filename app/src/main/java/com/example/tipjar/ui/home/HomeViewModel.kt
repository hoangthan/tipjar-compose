package com.example.tipjar.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipjar.domain.usecase.CalculatePaymentUseCase
import com.example.tipjar.domain.usecase.SavePaymentUseCase
import com.example.tipjar.ui.common.DispatcherProvider
import com.example.tipjar.ui.utils.asCurrencyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _viewEffect = MutableSharedFlow<HomeViewEffect>()
    val viewEffect = _viewEffect.asSharedFlow()

    init {
        listenStateChangeAndCalculate()
    }

    // Since we want to free the main thread from calculation task,
    // by listing the changes of state flow and calculation in Background then emit new value to Main Thread display
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
                val enableSave = internalState.billAmount.isNotEmpty()

                _state.update {
                    internalState.copy(
                        totalTip = totalTip,
                        perPerson = perPerson,
                        enableSave = enableSave
                    )
                }
            }
            .flowOn(dispatcherProvider.io)
            .launchIn(viewModelScope)
    }

    fun dispatchEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.OnSavePaymentClicked -> {
                handleOnPaymentClicked()
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

            is HomeViewEvent.SaveBillImage -> {
                _internalState.update { it.copy(billPhoto = event.imagePath) }
            }

            is HomeViewEvent.OnHistoryClicked -> {
                viewModelScope.launch {
                    _viewEffect.emit(HomeViewEffect.NavigateToHistory)
                }
            }
        }
    }

    private fun handleOnPaymentClicked() {
        val currentState = _state.value
        if (currentState.takePhotoReceipt && currentState.billPhoto.isNullOrEmpty()) {
            viewModelScope.launch { _viewEffect.emit(HomeViewEffect.LaunchCamera) }
        } else {
            savePayment()
        }
    }

    private fun savePayment() {
        viewModelScope.launch(dispatcherProvider.io) {
            val param = SavePaymentUseCase.SavePaymentData(
                billAmount = state.value.billAmount.toDoubleOrNull() ?: 0.0,
                tipAmount = state.value.tipPercent.toDoubleOrNull() ?: 0.0,
                billPhoto = state.value.billPhoto
            )
            savePaymentUseCase(param)
            _viewEffect.emit(HomeViewEffect.NavigateToHistory)
            _internalState.update { HomeViewState() }
        }
    }

    sealed interface HomeViewEffect {
        data object LaunchCamera : HomeViewEffect
        data object NavigateToHistory : HomeViewEffect
    }

    sealed interface HomeViewEvent {
        data object UpdateTakePhoto : HomeViewEvent
        data object OnSavePaymentClicked : HomeViewEvent
        data object OnHistoryClicked : HomeViewEvent
        data class UpdateAmount(val amount: String) : HomeViewEvent
        data class UpdateNumberOfPeople(val amount: Int) : HomeViewEvent
        data class UpdateTipPercent(val percent: String) : HomeViewEvent
        data class SaveBillImage(val imagePath: String?) : HomeViewEvent
    }

    data class HomeViewState(
        val tipPercent: String = "10",
        val billAmount: String = "",
        val numberOfPeople: Int = 1,
        val takePhotoReceipt: Boolean = false,
        val billPhoto: String? = null,
        val totalTip: String = "",
        val perPerson: String = "",
        val enableSave: Boolean = false,
    )
}
