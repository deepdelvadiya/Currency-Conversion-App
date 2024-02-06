package com.paypaytest.currencyconversionapp.data.database.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paypaytest.currencyconversionapp.data.entitys.Rate

class RateListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): List<Rate> {
        val type = object : TypeToken<List<Rate>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(rates: List<Rate>): String {
        return gson.toJson(rates)
    }
}
