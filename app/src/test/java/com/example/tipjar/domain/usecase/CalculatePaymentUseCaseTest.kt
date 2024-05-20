package com.example.tipjar.domain.usecase

import com.example.tipjar.domain.usecase.CalculatePaymentUseCase.CalculatePaymentParam
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Strict::class)
class CalculatePaymentUseCaseTest {

    private val useCase = CalculatePaymentUseCase()

    @Test
    fun invalid_numberOfPeople_throwsException() = runTest {
        val param = CalculatePaymentParam(
            amount = "10.0",
            tipPercent = "10.0",
            numberOfPeople = 0
        )

        Assert.assertThrows(AssertionError::class.java) {
            useCase(param)
        }
    }

    @Test
    fun default_tipPercent_throwsException() = runTest {
        val param = CalculatePaymentParam(
            amount = "10.0",
            tipPercent = "abc",
            numberOfPeople = 1
        )

        val result = useCase(param)
        Assert.assertTrue(result.totalTip == 0.0)
        Assert.assertTrue(result.costPerPerson == 10.0)
    }

    @Test
    fun default_billAmount_throwsException() = runTest {
        val param = CalculatePaymentParam(
            amount = "abc",
            tipPercent = "10.0",
            numberOfPeople = 1
        )

        val result = useCase(param)
        Assert.assertTrue(result.costPerPerson == 0.0)
    }

    @Test
    fun paramValid_return_result() = runTest {
        val param = CalculatePaymentParam(
            amount = "10.0",
            tipPercent = "10.0",
            numberOfPeople = 1
        )

        val result = useCase(param)
        Assert.assertTrue(result.costPerPerson == 11.0)
    }
}
