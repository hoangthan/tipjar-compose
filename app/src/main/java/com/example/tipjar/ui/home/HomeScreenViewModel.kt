package com.example.tipjar.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipjar.domain.usecase.CalculatePaymentUseCase
import com.example.tipjar.domain.usecase.CalculatePaymentUseCase.CalculatePaymentParam
import com.example.tipjar.domain.usecase.SavePaymentUseCase
import com.example.tipjar.ui.common.DispatcherProvider
import com.example.tipjar.ui.utils.asCurrencyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val savePaymentUseCase: SavePaymentUseCase,
    private val calculatePayment: CalculatePaymentUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state = _state.asStateFlow()

    val billData = state
        .map {
            val param = CalculatePaymentParam(
                amount = it.billAmount,
                tipPercent = it.tipPercent,
                numberOfPeople = it.numberOfPeople
            )

            val calculatePaymentResult = calculatePayment(param)

            CalculatedUiModel(
                calculatePaymentResult.totalTip.asCurrencyString(),
                calculatePaymentResult.costPerPerson.asCurrencyString()
            )
        }
        .flowOn(dispatcherProvider.default)
        .stateIn(viewModelScope, SharingStarted.Eagerly, CalculatedUiModel())

    fun dispatchEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.OnSavePaymentClicked -> {
                savePayment()
            }

            is HomeViewEvent.UpdateAmount -> {
                _state.update { it.copy(billAmount = event.amount) }
            }

            is HomeViewEvent.UpdateNumberOfPeople -> {
                val numberOfPeople = max(event.amount, 1)
                _state.update { it.copy(numberOfPeople = numberOfPeople) }
            }

            is HomeViewEvent.UpdateTakePhoto -> {
                val takePhoto = !state.value.takePhotoReceipt
                _state.update { it.copy(takePhotoReceipt = takePhoto) }
            }

            is HomeViewEvent.UpdateTipPercent -> {
                _state.update { it.copy(tipPercent = event.percent) }
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
            _state.update { HomeViewState() }
        }
    }

    sealed interface HomeViewEvent {
        data object UpdateTakePhoto : HomeViewEvent
        data object OnSavePaymentClicked : HomeViewEvent
        data class UpdateAmount(val amount: String) : HomeViewEvent
        data class UpdateNumberOfPeople(val amount: Int) : HomeViewEvent
        data class UpdateTipPercent(val percent: String) : HomeViewEvent
    }

    data class CalculatedUiModel(
        val totalTip: String = "",
        val perPerson: String = ""
    )

    data class HomeViewState(
        val tipPercent: String = "10",
        val billAmount: String = "",
        val numberOfPeople: Int = 1,
        val takePhotoReceipt: Boolean = false,
        val billPhoto: String? = null,
    )
}
