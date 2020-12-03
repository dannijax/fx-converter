package com.danijax.currencyconverter.model


/**
 * Manager for operations on Exchange rates
 */
class ExchangeRateManager(
    var baseCurrency: Currency = Currency("USD", "United States Of America"),
    private val quotes: Map<String, Float>,
    private val supportedCountries: List<Currency>
) {

    /**
     * Create an Exchange object from a given base currency
     * @param code secondary currency code
     * @return String Code combination
     */
    fun fromQuote(code: String): Exchange {
        val code1 = code.substring(0, 3)
        val code2 = code.substring(3, 6)
        val currency1 = supportedCountries.findLast {
            it.Code == code1
        }

        val currency2 = supportedCountries.findLast {
            it.Code == code2
        }
        return Exchange(currency1!!, currency2!!)
    }

    /**
     * Update the base currency to another given currency
     * @param currency the new base currency
     */
    fun updateBaseCurrency(currency: Currency) {
        baseCurrency = currency
    }

    /**
     * This Method uses the given base currency to create a new Map of exchange rates
     * If the Base Currency is no longer USD, it will extrapolate the new values and update the map
     */
    fun getQuotes(): MutableMap<String, Float> {
        //if the quotes or rates are not available? quit
        if (quotes.isNullOrEmpty()){
            return emptyMap<String, Float>().toMutableMap()
        }
        //If base currency is USD then just return the given quotes
        val rateMap = mutableMapOf<String, Float>()
        val usd = Currency("USD", "")
        if (baseCurrency.Code == "USD") return quotes.toMutableMap()

        //calculate the new rates for all other conversions and map out the new conversion rates
        val knownCurrencyForBase = Exchange(usd, baseCurrency)
        val knownRate = knownCurrencyForBase.rate(quotes)
        var reverseRate = 0.0F
        knownRate?.let {
            reverseRate = 1.div(it)
        }

        //Loop through the supported currencies and calculate rates
        supportedCountries.forEach { curr ->
            if (baseCurrency.Code != curr.Code) {
                val exchange1 = Exchange(usd, curr)
                val rate = exchange1.extrapolate(baseCurrency, quotes)
                rateMap[rate?.first!!] = rate.second
            } else {
                rateMap[baseCurrency.Code + curr.Code] = 1.0F
            }
        }
        rateMap[knownCurrencyForBase.reverseRate()] = reverseRate

        return rateMap
    }


}