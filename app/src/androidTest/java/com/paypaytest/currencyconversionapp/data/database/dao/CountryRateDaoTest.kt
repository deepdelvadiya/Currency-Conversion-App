package com.paypaytest.currencyconversionapp.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.paypaytest.currencyconversionapp.data.database.CurrencyAppDatabase
import com.paypaytest.currencyconversionapp.data.entitys.CodeEntity
import com.paypaytest.currencyconversionapp.data.entitys.CountryCodeEntity
import com.paypaytest.currencyconversionapp.data.entitys.Rate
import com.paypaytest.currencyconversionapp.data.entitys.RatesEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CountryRateDaoTest {

    private lateinit var rateDao: RateDao
    private lateinit var countryDao: CountryDao
    private lateinit var db: CurrencyAppDatabase

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            CurrencyAppDatabase::class.java,
        ).build()
        countryDao = db.currenciesDao()
        rateDao = db.ratesDao()
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun insertCountry_code_to_countryDao_and_verify() = runTest {
        val codeEntitys = listOf(
            CodeEntity("AED", "United Arab Emirates Dirham"),
            CodeEntity("AFN", "Afghan Afghani"),
            CodeEntity("ALL", "Albanian Lek"),
            CodeEntity("AMD", "Armenian Dram"),
            CodeEntity("ANG", "Netherlands Antillean Guilder"),
            CodeEntity("AOA", "Angolan Kwanza"),
            CodeEntity("ARS", "Argentine Peso    "),
            CodeEntity("AUD", "Australian Dollar"),
        )

        val countryCodeEntity = CountryCodeEntity(0, codeEntitys)

        countryDao.insertCurrencies(countryCodeEntity)

        val savedCountryCodeEntity = countryDao.getCurrencies().first()

        if (savedCountryCodeEntity != null) {
            assertEquals(CodeEntity("AED", "United Arab Emirates Dirham"),savedCountryCodeEntity.codeEntity.first())
            assertNotEquals(CodeEntity("AED", "United Arab Emirates"),savedCountryCodeEntity.codeEntity.first())
        }
    }

    @Test
    fun insertCountry_currencies_to_ratesDao_and_verify() = runTest {
        val rates = listOf(
            Rate("AED", 3.673),
            Rate("AFN", 72.061495),
            Rate("ALL", 95.244649),
            Rate("AMD", 403.970108),
            Rate("ANG", 1.799861),
            Rate("AOA", 829.684067),
            Rate("ARS", 819.776325),
            Rate("AUD", 1.517964)
        )


        val ratesEntity = RatesEntity(
            0,
            "Usage subject to terms: https://openexchangerates.org/terms",
            "https://openexchangerates.org/license",
            1705921200,
            "USD",
            rates
        )

        rateDao.insertRates(ratesEntity)

        val savedRatesEntity = rateDao.getCountryRates().first()

        if (savedRatesEntity != null){
            assertEquals( Rate("AED", 3.673),savedRatesEntity.rates.first())
            assertNotEquals( Rate("AED", 3.73),savedRatesEntity.rates.first())
        }
    }
}