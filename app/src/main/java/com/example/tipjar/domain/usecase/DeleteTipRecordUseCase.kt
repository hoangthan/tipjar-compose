package com.example.tipjar.domain.usecase

import com.example.tipjar.domain.repository.TipRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteTipRecordUseCase @Inject constructor(
    private val paymentRepository: TipRepository,
) {
    suspend operator fun invoke(id: Long) = paymentRepository.deleteById(id)
}
