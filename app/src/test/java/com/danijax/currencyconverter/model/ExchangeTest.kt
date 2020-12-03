package com.danijax.currencyconverter.model


import com.danijax.currencyconverter.toDateTime
import com.danijax.currencyconverter.toTimeStamp
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ExchangeTest {
    private val usCurrency = Currency(Code = "USD", Name = "United States Dollar")
    private val chCurrency = Currency(Code = "CNY", Name = "Chinese Yuan")
    private val rateMap = Json.decodeFromString<HashMap<String, Float>>("{\"USDAED\": 3.672978, \"USDAFN\": 77.00000, \"USDNGN\": 382, \"USDGBP\": 0.750825, \"USDJPY\": 104.031041}")
    private val nairaToPounds = 0.0020F

    @Test
    fun `test currency combination`(){
        val exchange = Exchange(usCurrency, chCurrency)
        assertTrue(exchange.rateCode() == "USDCNY")
    }

    @Test
    fun `test currency reverse combination`(){

        val exchange = Exchange(usCurrency, chCurrency)
        assertTrue(exchange.reverseRate() == "CNYUSD")
    }

    @Test
    fun `test get correct xchange rate`(){
        val usCurrency = Currency(Code = "USD", Name = "United States Dollar")
        val aedCurrency = Currency(Code = "AED", Name = "AEN")
        val afnCurrency = Currency(Code = "AFN", Name = "AFN")
        val exchange1 = Exchange(usCurrency, aedCurrency)
        val exchange2 = Exchange(usCurrency, afnCurrency)
        val rate = exchange1.rate(rateMap) ?: 0.00F
        val rate2 =exchange2.rate(rateMap) ?: 0.00F
        println(rate)
        println(rate2)
        assertTrue(rate.equals(3.672978F))
        assertTrue(rate2.equals(77.00000F))

    }

    @Test
    fun `test extrapolation of exchange rate value based on given one`(){
        val usCurrency = Currency(Code = "USD", Name = "United States Dollar")
        val gbpCurrency = Currency(Code = "GBP", Name = "United Kingdom")
        val nairaCurrency = Currency("NGN", "Nigeria")
        val mainExchange = Exchange(usCurrency, gbpCurrency)
        val secondaryExchange = Exchange(usCurrency, nairaCurrency)
        val rate = mainExchange.rate(rateMap) ?: 0.00F
        val rate2 = secondaryExchange.rate(rateMap) ?: 0.00F
        val ngngbpExtrapolatd = mainExchange.extrapolate(nairaCurrency, rateMap)
        assertTrue(ngngbpExtrapolatd?.first == "NGNGBP")
        print(ngngbpExtrapolatd?.second)
        assertTrue(ngngbpExtrapolatd?.second == nairaToPounds)
    }

    @Test
    fun `test conversion of USD to JPY`(){
        val usCurrency = Currency("USD", "United State of America")
        val jpyCurrency = Currency("JPY", "Japan")
        val exchange = Exchange(usCurrency, jpyCurrency)
        val conversion = exchange.convert(100F, rateMap, exchange.rateCode() )
        print(conversion)
        assertTrue(conversion == 10403.10F)

    }

    @Test
    fun `test serialization`(){
        val json = "{\n" +
                "    \"success\": true,\n" +
                "    \"terms\": \"https://currencylayer.com/terms\",\n" +
                "    \"privacy\": \"https://currencylayer.com/privacy\",\n" +
                "    \"timestamp\": 1430401802,\n" +
                "    \"source\": \"USD\",\n" +
                "    \"quotes\": {\n" +
                "        \"USDAED\": 3.672982,\n" +
                "        \"USDAFN\": 57.8936,\n" +
                "        \"USDALL\": 126.1652,\n" +
                "        \"USDAMD\": 475.306,\n" +
                "        \"USDANG\": 1.78952,\n" +
                "        \"USDAOA\": 109.216875,\n" +
                "        \"USDARS\": 8.901966,\n" +
                "        \"USDAUD\": 1.269072,\n" +
                "        \"USDAWG\": 1.792375,\n" +
                "        \"USDAZN\": 1.04945,\n" +
                "        \"USDBAM\": 1.757305,\n" +
                "    [...]\n" +
                "    }\n" +
                "}  "

        val model = Json.decodeFromString<CurrencyResponseModel>(json)
    }

    @Test
    fun `test time diff`() {
        val t1 =LocalDateTime.parse("2020-11-30T10:19:05.118")
        val t2 = LocalDateTime.now()
        val t3 = 1606668070L.toDateTime()
        //print(t2)
        val diff = ChronoUnit.MINUTES.between(t1, t2)
        println(diff)
        println(t3.toTimeStamp())
        println(t1.toTimeStamp())
        println(t2.toTimeStamp())
    }

}