package com.example.tipjar.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculatePaymentUseCase @Inject constructor() {

    operator fun invoke(param: CalculatePaymentParam): CalculatePaymentResult {
        val numberOfPeople = param.numberOfPeople
        assert(numberOfPeople >= 1) { "Invalid number of people" }
        val amount = param.amount?.toDoubleOrNull() ?: 0.0
        val percent = param.tipPercent?.toDoubleOrNull() ?: 0.0
        val totalTip = amount * percent / 100
        val costPerPerson = (amount + totalTip) / numberOfPeople
        return CalculatePaymentResult(totalTip, costPerPerson)
    }

    data class CalculatePaymentParam(
        val amount: String?,
        val tipPercent: String?,
        val numberOfPeople: Int,
    )

    data class CalculatePaymentResult(
        val totalTip: Double,
        val costPerPerson: Double,
    )
}
