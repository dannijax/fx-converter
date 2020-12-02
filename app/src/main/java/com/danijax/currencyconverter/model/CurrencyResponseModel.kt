package com.danijax.currencyconverter.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponseModel(
    val success: Boolean,
    val terms: String = "",
    val privacy: String = "",
    val timestamp: Long = 0,
    val source: String = "",
    val quotes: Map<String, Float> = mapOf(),
    val error: Error? = null
)

@Serializable
data class Error(val code: Int, val info: String)

