package com.danijax.currencyconverter.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class ExchangeRateManagerTest{

    private val rateMap = Json.decodeFromString<HashMap<String, Float>>("{\"USDAED\": 3.672978, \"USDAFN\": 77.00000, \"USDNGN\": 382, \"USDGBP\": 0.750825, \"USDJPY\": 104.031041}")
    private val countries = Json.decodeFromString<List<Currency>>("[\n" +
            "{\n" +
            "    \"Code\": \"USD\",\n" +
            "    \"Name\": \"United States of America\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"Code\": \"AED\",\n" +
            "    \"Name\": \"United Arab Emirates Dirham\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"Code\": \"AFN\",\n" +
            "    \"Name\": \"Afghan Afghani\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"Code\": \"NGN\",\n" +
            "    \"Name\": \"Albanian Lek\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"Code\": \"GBP\",\n" +
            "    \"Name\": \"Armenian Dram\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"Code\": \"JPY\",\n" +
            "    \"Name\": \"Netherlands Antillean Guilder\"\n" +
            "  } \n ]")


    @Test
    fun `test creating exchange from code combination`(){
        val manager = ExchangeRateManager(quotes = rateMap, supportedCountries = countries)
        val exchange = manager.fromQuote("USDAED")
        println(exchange.rateCode())
        println(manager.getQuotes())

        manager.updateBaseCurrency(Currency("JPY", ""))
        println(manager.getQuotes())
    }


}