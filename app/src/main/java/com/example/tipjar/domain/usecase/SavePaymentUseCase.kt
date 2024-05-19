package com.example.tipjar.domain.usecase

import com.example.tipjar.domain.repository.TipRepository
import javax.inject.Inject

class SavePaymentUseCase @Inject constructor(
    private val paymentRepository: TipRepository
) {
    suspend operator fun invoke(data: SavePaymentData) {
        paymentRepository.saveTip(
            billAmount = data.billAmount,
            tipAmount = data.tipAmount,
            imageUrl = data.billPhoto
        )
    }

    data class SavePaymentData(
        val billAmount: Double,
        val tipAmount: Double,
        val billPhoto: String? = null,
    )
}
