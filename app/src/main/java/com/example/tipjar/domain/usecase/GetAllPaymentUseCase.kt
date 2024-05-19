package com.example.tipjar.domain.usecase

import com.example.tipjar.domain.repository.TipRepository
import javax.inject.Inject

class GetAllPaymentUseCase @Inject constructor(
    private val paymentRepository: TipRepository,
) {
    suspend operator fun invoke() = paymentRepository.getAll()
}
