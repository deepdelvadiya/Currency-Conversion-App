package com.paypaytest.currencyconversionapp.data.remote

import com.paypaytest.currencyconversionapp.common.constant.apiIdParam
import com.paypaytest.currencyconversionapp.data.entitys.CountryCodeEntity
import com.paypaytest.currencyconversionapp.data.entitys.RatesEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenExchangeRatesApis {

    @GET("/currencies.json")
    suspend fun getCurrencies(
        @Query("") apiId:String
    ):Response<CountryCodeEntity>


    @GET("/latest.json")
    suspend fun getLatestCurrenciesValue(
        @Query(apiIdParam) apiId: String
    ):Response<RatesEntity>
}