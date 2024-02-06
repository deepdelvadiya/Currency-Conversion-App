package com.paypaytest.currencyconversionapp.domain.repository

import com.paypaytest.currencyconversionapp.data.utils.Syncable
import com.paypaytest.currencyconversionapp.domain.model.CountryModel
import com.paypaytest.currencyconversionapp.domain.model.RateModel
import kotlinx.coroutines.flow.Flow


interface CurrencyAppRepository: Syncable {

    fun getCurrencies(): Flow<List<CountryModel>>

    fun getRates():Flow<List<RateModel>>
}