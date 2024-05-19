package com.example.tipjar.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipjar.domain.usecase.DeleteTipRecordUseCase
import com.example.tipjar.domain.usecase.GetAllTipHistoryUseCase
import com.example.tipjar.ui.common.DispatcherProvider
import com.example.tipjar.ui.history.model.TipUiModel
import com.example.tipjar.ui.history.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val loadHistoryUseCase: GetAllTipHistoryUseCase,
    private val deletePaymentUseCase: DeleteTipRecordUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _viewState = MutableStateFlow(HistoryViewState())
    val viewState = _viewState.asStateFlow()

    init {
        collectHistoryData()
    }

    private fun collectHistoryData() {
        loadHistoryUseCase()
            .onEach { records ->
                val uiModels = records.map { it.toUiModel() }
                _viewState.update { it.copy(payments = uiModels) }
            }
            .flowOn(dispatcherProvider.io)
            .launchIn(viewModelScope)
    }

    fun dispatchViewEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.DeleteTipRecord -> deletePayment(event.id)
        }
    }

    private fun deletePayment(id: Long) {
        viewModelScope.launch(dispatcherProvider.io) {
            deletePaymentUseCase(id)
        }
    }

    sealed interface ViewEvent {
        data class DeleteTipRecord(val id: Long) : ViewEvent
    }

    data class HistoryViewState(
        val payments: List<TipUiModel> = emptyList()
    )
}
