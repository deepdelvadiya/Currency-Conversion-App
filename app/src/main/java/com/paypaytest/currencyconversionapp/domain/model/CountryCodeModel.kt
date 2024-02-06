package com.paypaytest.currencyconversionapp.domain.model

import com.paypaytest.currencyconversionapp.data.entitys.CodeEntity

data class CountryModel(
    val code: String
)

fun CodeEntity.toCountryModel():CountryModel{
   return CountryModel(
        code = code
    )
}