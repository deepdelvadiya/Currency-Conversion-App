package com.paypaytest.currencyconversionapp.data.entitys
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity("RateTable")
data class RatesEntity(
	@PrimaryKey
	val id:Int,
	val disclaimer: String,
	val license: String,
	val timestamp: Long,
	val base: String,
	val rates: List<Rate>
)

@JsonClass(generateAdapter = true)
data class Rate(
	val currencyCode: String,
	val rate: Double
)