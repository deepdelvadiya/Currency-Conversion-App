package com.paypaytest.currencyconversionapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.paypaytest.currencyconversionapp.data.entitys.CountryCodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Transaction
    @Query(value = "SELECT * FROM CountryCode LIMIT 1")
    fun getCurrencies(): Flow<CountryCodeEntity?>

    @Upsert
    suspend fun insertCurrencies(countryCodeEntity:CountryCodeEntity)
}