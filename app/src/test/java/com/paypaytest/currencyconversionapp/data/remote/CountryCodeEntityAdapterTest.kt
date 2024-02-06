package com.paypaytest.currencyconversionapp.data.remote

import com.paypaytest.currencyconversionapp.data.entitys.CodeEntity
import com.paypaytest.currencyconversionapp.data.remote.moshi_adapters.CountryCodeEntityAdapter
import com.squareup.moshi.JsonReader
import okio.Buffer
import org.junit.Assert.assertEquals
import org.junit.Test

class CountryCodeEntityAdapterTest {

    @Test
    fun testFromJson() {
        // Mock JSON data
        val jsonString = """
            {
                "USD": "United States Dollar",
                "EUR": "Euro",
                "JPY": "Japanese Yen"
            }
        """.trimIndent()

        // Create a JsonReader from the mock JSON data
        val jsonReader = JsonReader.of(Buffer().writeUtf8(jsonString))


        // Create an instance of the adapter
        val adapter = CountryCodeEntityAdapter()

        // Call the fromJson method
        val countryCodeEntity = adapter.fromJson(jsonReader)

        // Verify the result
        assertEquals(1, countryCodeEntity.id)
        assertEquals(3, countryCodeEntity.codeEntity?.size)

        // Verify specific codes and names
        assertEquals(CodeEntity("USD", "United States Dollar"), countryCodeEntity.codeEntity[0])
        assertEquals(CodeEntity("EUR", "Euro"), countryCodeEntity.codeEntity[1])
        assertEquals(CodeEntity("JPY", "Japanese Yen"), countryCodeEntity.codeEntity[2])
    }
}
