package com.danijax.currencyconverter.data

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class ErrorStatesTest {

    @Test
    fun `test sealed class serialization`(){
        val errorStates = ErrorStates.DeveloperError(104)
        val serialized = Json.encodeToString(errorStates)
        val deserialized = Json.decodeFromString<ErrorStates.DeveloperError>(serialized)
        print(serialized)
        assertTrue(errorStates == deserialized)
    }
}