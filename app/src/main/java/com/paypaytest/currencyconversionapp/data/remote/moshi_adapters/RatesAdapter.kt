package com.paypaytest.currencyconversionapp.data.remote.moshi_adapters


import com.paypaytest.currencyconversionapp.data.entitys.Rate
import com.paypaytest.currencyconversionapp.data.entitys.RatesEntity
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import okio.IOException
import javax.inject.Inject



class RatesAdapter @Inject constructor() : JsonAdapter<RatesEntity>() {

    @FromJson
    override fun fromJson(reader: JsonReader): RatesEntity {
        reader.beginObject()

        var disclaimer = ""
        var license = ""
        var timestamp = 0L
        var base = ""
        var rates: List<Rate>? = null

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "disclaimer" -> disclaimer = reader.nextString()
                "license" -> license = reader.nextString()
                "timestamp" -> timestamp = reader.nextLong()
                "base" -> base = reader.nextString()
                "rates" -> rates = readRates(reader)
                else -> reader.skipValue()
            }
        }

        reader.endObject()

        return RatesEntity(0,disclaimer, license, timestamp, base, rates ?: emptyList())
    }

    private fun readRates(reader: JsonReader): List<Rate> {
        val rateList = mutableListOf<Rate>()

        reader.beginObject()
        while (reader.hasNext()) {
            val currencyCode = reader.nextName()
            val rate = reader.nextDouble()
            rateList.add(Rate(currencyCode, rate))
        }
        reader.endObject()

        return rateList
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: RatesEntity?) {
        if (value == null) {
            throw NullPointerException("value == null")
        }
    }
}
