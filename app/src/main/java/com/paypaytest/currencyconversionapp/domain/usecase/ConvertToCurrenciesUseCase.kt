package com.paypaytest.currencyconversionapp.domain.usecase

import com.paypaytest.currencyconversionapp.common.result.Result
import com.paypaytest.currencyconversionapp.common.result.asResult
import com.paypaytest.currencyconversionapp.domain.model.ConvertedRateModel
import com.paypaytest.currencyconversionapp.domain.model.CountryModel
import com.paypaytest.currencyconversionapp.domain.repository.CurrencyAppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.seconds

class ConvertToCurrenciesUseCase @Inject constructor(
    private val currencyAppRepository: CurrencyAppRepository
) {

    operator fun invoke(
        selectedCountryCode: CountryModel,
        amount: String
    ): Flow<Result<List<ConvertedRateModel>>> {
        val convertedRate = currencyAppRepository.getRates().map { ratesList ->
            ratesList.map {
                val output =
                    amount.toDouble() / (ratesList.find { it.countryCode == selectedCountryCode.code }?.rates
                        ?: throw NullPointerException("Can't not find currency base")) * it.rates
                ConvertedRateModel(
                    countryCode = it.countryCode,
                    rates = output
                )
            }
        }
        return convertedRate.asResult()
    }
}