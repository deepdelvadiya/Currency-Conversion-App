package com.paypaytest.currencyconversionapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.paypaytest.currencyconversionapp.data.entitys.RatesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RateDao {

    @Transaction
    @Query(value = "SELECT * FROM RateTable LIMIT 1")
    fun getCountryRates(): Flow<RatesEntity?>

    @Upsert
    suspend fun insertRates(ratesEntity: RatesEntity)
}