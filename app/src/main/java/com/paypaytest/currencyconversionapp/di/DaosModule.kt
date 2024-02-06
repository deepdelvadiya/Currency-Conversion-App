package com.paypaytest.currencyconversionapp.di

import com.paypaytest.currencyconversionapp.data.database.CurrencyAppDatabase
import com.paypaytest.currencyconversionapp.data.database.dao.CountryDao
import com.paypaytest.currencyconversionapp.data.database.dao.RateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesTopicsDao(
        database: CurrencyAppDatabase,
    ): CountryDao = database.currenciesDao()

    @Provides
    fun providesRatesDao(
        database: CurrencyAppDatabase,
    ): RateDao = database.ratesDao()

}