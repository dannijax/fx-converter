package com.danijax.currencyconverter

import com.danijax.currencyconverter.model.Currency
import org.junit.Assert.*
import org.junit.Test

class UtilitiesKtTest {

    @Test
    fun `test dynamic url generation`(){
        val currency = Currency("NGN", "Nigeria")
        println(currency.generateFlagUrl())
        assertTrue(currency.generateFlagUrl() == "http://www.geognos.com/api/en/countries/flag/NG.png")
    }

    @Test
    fun `test number formating to exponetial form`(){
        val number = "100000000"

        println(number.formatExponentialForm())

    }
}