package com.paypaytest.currencyconversionapp.data.database.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paypaytest.currencyconversionapp.data.entitys.CodeEntity
import com.paypaytest.currencyconversionapp.data.entitys.CountryCodeEntity

class CuntryCodeTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): List<CodeEntity> {
        val type = object : TypeToken<List<CodeEntity>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(rates: List<CodeEntity>): String {
        return gson.toJson(rates)
    }
}