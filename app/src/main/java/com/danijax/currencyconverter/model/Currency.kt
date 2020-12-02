package com.danijax.currencyconverter.model

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
data class Currency(val Code: String, val Name: String)