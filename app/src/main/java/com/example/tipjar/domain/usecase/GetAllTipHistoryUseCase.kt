package com.example.tipjar.domain.usecase

import com.example.tipjar.domain.repository.TipRepository
import javax.inject.Inject

class GetAllTipHistoryUseCase @Inject constructor(
    private val paymentRepository: TipRepository,
) {
    operator fun invoke() = paymentRepository.getAll()
}
