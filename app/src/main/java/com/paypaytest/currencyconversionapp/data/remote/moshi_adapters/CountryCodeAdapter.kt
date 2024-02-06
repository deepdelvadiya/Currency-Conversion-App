package com.paypaytest.currencyconversionapp.data.remote.moshi_adapters

import com.paypaytest.currencyconversionapp.data.entitys.CodeEntity
import com.paypaytest.currencyconversionapp.data.entitys.CountryCodeEntity
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson


class CountryCodeEntityAdapter : JsonAdapter<CountryCodeEntity>() {

    @FromJson
    override fun fromJson(reader: JsonReader): CountryCodeEntity {
        val codeEntityList = mutableListOf<CodeEntity>()

        reader.beginObject()
        while (reader.hasNext()) {
            val currencyCode = reader.nextName()
            val currencyName = reader.nextString()
            codeEntityList.add(CodeEntity(currencyCode, currencyName))
        }
        reader.endObject()

        return CountryCodeEntity(1, codeEntityList) // You may customize the ID accordingly
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: CountryCodeEntity?) {
        throw UnsupportedOperationException("Not implemented")
    }
}
