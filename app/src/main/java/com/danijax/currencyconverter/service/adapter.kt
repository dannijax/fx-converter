package com.danijax.currencyconverter.service

import kotlinx.serialization.Serializable

@Serializable
sealed class Result<out T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Failure(val statusCode: Int?, val message: String? = null) : Result<Nothing>()
    object NetworkError : Result<Nothing>()
}





