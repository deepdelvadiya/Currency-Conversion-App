package com.paypaytest.currencyconversionapp.data.database

import com.paypaytest.currencyconversionapp.data.database.typeconverter.RateListConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paypaytest.currencyconversionapp.data.database.dao.CountryDao
import com.paypaytest.currencyconversionapp.data.database.dao.RateDao
import com.paypaytest.currencyconversionapp.data.database.typeconverter.CuntryCodeTypeConverter
import com.paypaytest.currencyconversionapp.data.entitys.CountryCodeEntity
import com.paypaytest.currencyconversionapp.data.entitys.RatesEntity

@Database(
    entities = [CountryCodeEntity::class, RatesEntity::class],
    version = 3,
    autoMigrations = [],
    exportSchema = false,
)
@TypeConverters(RateListConverter::class,CuntryCodeTypeConverter::class)
abstract class CurrencyAppDatabase:RoomDatabase() {

    abstract fun currenciesDao():CountryDao

    abstract fun ratesDao():RateDao
}