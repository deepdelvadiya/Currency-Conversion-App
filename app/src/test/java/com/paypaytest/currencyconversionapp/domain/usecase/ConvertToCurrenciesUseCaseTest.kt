package com.paypaytest.currencyconversionapp.domain.usecase

import com.paypaytest.currencyconversionapp.common.result.Result
import com.paypaytest.currencyconversionapp.data.repository.FakeCurrencyAppRepository
import com.paypaytest.currencyconversionapp.domain.model.ConvertedRateModel
import com.paypaytest.currencyconversionapp.domain.model.CountryModel
import com.paypaytest.currencyconversionapp.domain.repository.CurrencyAppRepository
import com.paypaytest.currencyconversionapp.domain.usecase.ConvertToCurrenciesUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConvertToCurrenciesUseCaseTest {
    private val fakeRepository: CurrencyAppRepository = FakeCurrencyAppRepository()
    private val useCase = ConvertToCurrenciesUseCase(fakeRepository)

    @Test
    fun `invoke should return converted rates`() = runTest {
        // Invoke the use case
        val selectedCountryCode = CountryModel("USD")
        val amount = "10.0"
        useCase(selectedCountryCode, amount).collectLatest {
            if (it is Result.Success) {
                val convertedRates = it.data
                assertEquals(fakeRepository.getRates().first().size, convertedRates.size)
            }
        }
    }

    @Test
    fun `invoke should throw an error of currency not found`() = runTest {
        // Invoke the use case
        val selectedCountryCode = CountryModel("ABD")
        val amount = "10.0"
        useCase(selectedCountryCode, amount).collectLatest {

            if (it is Result.Error) {
                assertEquals("Can't not find currency base", it.exception)
            }

        }
    }

}