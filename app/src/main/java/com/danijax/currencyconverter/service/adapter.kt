package com.danijax.currencyconverter.service

import kotlinx.serialization.Serializable
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Serializable
sealed class Result<out T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Failure(val statusCode: Int?, val message: String? = null) : Result<Nothing>()
    object NetworkError : Result<Nothing>()
}





