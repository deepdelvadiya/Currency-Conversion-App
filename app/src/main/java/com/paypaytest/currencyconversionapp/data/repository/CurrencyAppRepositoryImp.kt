package com.paypaytest.currencyconversionapp.data.repository

import android.util.Log
import com.paypaytest.currencyconversionapp.common.DispatcherProvider
import com.paypaytest.currencyconversionapp.data.database.dao.CountryDao
import com.paypaytest.currencyconversionapp.data.database.dao.RateDao
import com.paypaytest.currencyconversionapp.data.remote.OpenExchangeRatesApis
import com.paypaytest.currencyconversionapp.domain.model.CountryModel
import com.paypaytest.currencyconversionapp.domain.model.RateModel
import com.paypaytest.currencyconversionapp.domain.model.toCountryModel
import com.paypaytest.currencyconversionapp.domain.model.toRateModel
import com.paypaytest.currencyconversionapp.domain.repository.CurrencyAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlin.math.log

class CurrencyAppRepositoryImp @Inject constructor(
    private val openExchangeRatesApis: OpenExchangeRatesApis,
    private val countryDao: CountryDao,
    private val rateDao: RateDao,
    private val dispatcherProvider: DispatcherProvider
) : CurrencyAppRepository {

    override fun getCurrencies(): Flow<List<CountryModel>> {
        return countryDao.getCurrencies().map { it?.codeEntity?.map { it.toCountryModel() } ?: emptyList() }
    }

    override fun getRates(): Flow<List<RateModel>> {
        return rateDao.getCountryRates().map { it?.rates?.map { it.toRateModel() } ?: emptyList() }
    }

    override suspend fun syncWith(apiId: String): Boolean {
        return withContext(dispatcherProvider.io) {
            try {
                val countryCodeResponse = openExchangeRatesApis.getCurrencies(apiId)
                val ratesResponse = openExchangeRatesApis.getLatestCurrenciesValue(apiId)
                if (ratesResponse.isSuccessful && countryCodeResponse.isSuccessful) {
                    val countryCode = countryCodeResponse.body()
                    val rates = ratesResponse.body()
                    if (rates != null && countryCode != null) {
                        Log.d("DEEP", "syncWith: Completed ")
                        Log.d("DEEP", "syncWith: $countryCode. and $rates")
                        rateDao.insertRates(rates)
                        countryDao.insertCurrencies(countryCode)
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }

            } catch (exception: Exception) {
                if (exception is CancellationException) {
                    throw exception
                }
                Log.i("DEEP", "syncWith: ${exception.localizedMessage}")
                false
            }
        }
    }


}