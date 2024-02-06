package com.paypaytest.currencyconversionapp.data.remote

import com.paypaytest.currencyconversionapp.data.entitys.Rate
import com.paypaytest.currencyconversionapp.data.remote.moshi_adapters.RatesAdapter
import com.squareup.moshi.JsonReader
import okio.Buffer
import org.junit.Assert.assertEquals
import org.junit.Test

class RatesAdapterTest {

    @Test
    fun testFromJson() {
        // Mock JSON data
        val jsonString = """
            {
                "disclaimer": "Some disclaimer",
                "license": "Some license",
                "timestamp": 1619568000,
                "base": "USD",
                "rates": {
                    "EUR": 0.85,
                    "JPY": 109.3
                }
            }
        """.trimIndent()

        // Create a JsonReader from the mock JSON data
        val jsonReader = JsonReader.of(Buffer().writeUtf8(jsonString))

        // Create an instance of the adapter
        val adapter = RatesAdapter()

        // Call the fromJson method
        val ratesEntity = adapter.fromJson(jsonReader)

        // Verify the result
        assertEquals(0, ratesEntity.id)
        assertEquals("Some disclaimer", ratesEntity.disclaimer)
        assertEquals("Some license", ratesEntity.license)
        assertEquals(1619568000, ratesEntity.timestamp)
        assertEquals("USD", ratesEntity.base)
        assertEquals(2, ratesEntity.rates.size)

        // Verify specific rates
        assertEquals(Rate("EUR", 0.85), ratesEntity.rates[0])
        assertEquals(Rate("JPY", 109.3), ratesEntity.rates[1])
    }

    @Test
    fun testFromJsonEmptyRates() {
        // Mock JSON data with empty rates
        val jsonString = """
            {
                "disclaimer": "Some disclaimer",
                "license": "Some license",
                "timestamp": 1619568000,
                "base": "USD",
                "rates": {}
            }
        """.trimIndent()

        // Create a JsonReader from the mock JSON data
        val jsonReader = JsonReader.of(Buffer().writeUtf8(jsonString))

        // Create an instance of the adapter
        val adapter = RatesAdapter()

        // Call the fromJson method
        val ratesEntity = adapter.fromJson(jsonReader)

        // Verify the result
        assertEquals(0, ratesEntity.id)
        assertEquals("Some disclaimer", ratesEntity.disclaimer)
        assertEquals("Some license", ratesEntity.license)
        assertEquals(1619568000, ratesEntity.timestamp)
        assertEquals("USD", ratesEntity.base)
        assertEquals(0, ratesEntity.rates.size)
    }
}
