package com.danijax.currencyconverter.data

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class ErrorStatesTest {

    @Test
    fun `test sealed class serialization for Developer Error state`(){
        val errorStatesDeveloper = ErrorStates.DeveloperError(104)
        val serialized = Json.encodeToString(errorStatesDeveloper)
        val deserialized = Json.decodeFromString<ErrorStates.DeveloperError>(serialized)
        assertTrue(errorStatesDeveloper == deserialized)
    }

    @Test
    fun `test sealed class serialization for Network Error state`(){
        val errorStatesNetwork = ErrorStates.NetworkError(1000)
        val serialized = Json.encodeToString(errorStatesNetwork)
        val deserialized = Json.decodeFromString<ErrorStates.NetworkError>(serialized)
        assertTrue(errorStatesNetwork == deserialized)
    }

    @Test
    fun `test sealed class serialization for Unknown Error state`(){
        val errorStatesUnknown = ErrorStates.UnKnownError(1000)
        val serialized = Json.encodeToString(errorStatesUnknown)
        val deserialized = Json.decodeFromString<ErrorStates.UnKnownError>(serialized)
        assertTrue(errorStatesUnknown == deserialized)
    }
}