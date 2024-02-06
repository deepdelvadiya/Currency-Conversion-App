package com.paypaytest.currencyconversionapp.data.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.paypaytest.currencyconversionapp.domain.model.CountryModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.reflect.KProperty1

@Entity(
	tableName = "CountryCode"
)

@JsonClass(generateAdapter = true)
data class CountryCodeEntity(
	@PrimaryKey(autoGenerate = false)
	val id:Int, // Add the id field for Room
	val codeEntity: List<CodeEntity>
)

@JsonClass(generateAdapter = true)
data class CodeEntity(
	val code:String,
	val name: String
)
