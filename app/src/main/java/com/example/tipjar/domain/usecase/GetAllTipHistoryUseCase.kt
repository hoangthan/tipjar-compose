package com.example.tipjar.domain.usecase

import com.example.tipjar.domain.repository.TipRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllTipHistoryUseCase @Inject constructor(
    private val paymentRepository: TipRepository,
) {
    operator fun invoke() = paymentRepository.getAll()
}
