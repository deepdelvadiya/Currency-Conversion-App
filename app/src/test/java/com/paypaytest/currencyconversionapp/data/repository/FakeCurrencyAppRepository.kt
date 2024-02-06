package com.paypaytest.currencyconversionapp.data.repository
import com.paypaytest.currencyconversionapp.data.entitys.CodeEntity
import com.paypaytest.currencyconversionapp.data.entitys.CountryCodeEntity
import com.paypaytest.currencyconversionapp.data.entitys.Rate
import com.paypaytest.currencyconversionapp.domain.model.CountryModel
import com.paypaytest.currencyconversionapp.domain.model.RateModel
import com.paypaytest.currencyconversionapp.domain.model.toCountryModel
import com.paypaytest.currencyconversionapp.domain.model.toRateModel
import com.paypaytest.currencyconversionapp.domain.repository.CurrencyAppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeCurrencyAppRepository @Inject constructor() : CurrencyAppRepository {
    private val fakeCurrencies = listOf(
        CodeEntity("USD", "United States Dollar"),
        CodeEntity("EUR", "Euro"),
        // Add more fake countries as needed
    )

    private val fakeRates = listOf(
        Rate("USD", 1.0),
        Rate("EUR", 0.916721),
        // Add more fake rates as needed
    )

    override fun getCurrencies(): Flow<List<CountryModel>> = flow {
        emit(fakeCurrencies.map { it.toCountryModel() })
    }

    override fun getRates(): Flow<List<RateModel>> = flow {
        emit(fakeRates.map { it.toRateModel() })
    }

    override suspend fun syncWith(apiId: String): Boolean {
        return true
    }
}
