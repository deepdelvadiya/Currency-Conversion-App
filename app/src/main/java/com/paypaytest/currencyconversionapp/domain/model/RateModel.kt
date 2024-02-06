package com.paypaytest.currencyconversionapp.domain.model

import com.paypaytest.currencyconversionapp.data.entitys.Rate

data class RateModel(
    val countryCode:String,
    val rates:Double
)

fun Rate.toRateModel():RateModel{
    return RateModel(
        countryCode = currencyCode,
        rates =  rate
    )
}
