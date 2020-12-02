package com.danijax.currencyconverter.model

import java.math.BigDecimal
import java.math.RoundingMode

class Exchange(private val from: Currency, private val to: Currency) {

    /**
     * Generate exchange code combination e.g USDGBP
     */
    fun rateCode() = from.Code.plus(to.Code)

    /**
     * Generate reverse exchange code combination eg USDGBP -> GBPUSD
     * @return the string combination of currency code in reverse
     */
    fun reverseRate() = to.Code.plus(from.Code)

    /**
     * Get Exchange rate value from the key mappings
     * @param map contains all the given exchange rates
     * @return Float exchange rate value
     */
    fun rate(map: Map<String, Float>): Float? {
        return map[rateCode()]
    }

    /**
     * Get the conversion value of a given amount
     * @param value the given amount to be converted
     * @param map key-value mappings of exchange rate
     * @param currencyCombo the code combination between 2 currencies
     * @return the conversion amount
     */
    fun convert(value: Float, map: Map<String, Float>, currencyCombo: String): Float {
        val changeRate = map[currencyCombo] ?: 0.00
        return formatToDP(value = value * changeRate.toFloat()).toFloat()
    }

    /**
     * Based on the given exchange rates we can extrapolate to get the value of the exchange rate
     * between 2 other currencies
     * @param currency the base currency to extrapolate to
     * @param map exchange rates key-mapping
     * @sample extrapolate(NGNGBP) given we have reates for USDGBP, USDNGN we can extrapolate to get
     * NGNGBP
     * @return Pair of Currency code combination to Exchange rate value
     */
    fun extrapolate(currency: Currency, map: Map<String, Float>): Pair<String, Float>? {
        if (currency == from || currency == to) {
            return null
        }

        val exchangeRate1 = map[rateCode()] ?: 0.00
        val exchange2 = map[Exchange(from, currency).rateCode()] ?: 0.00
        val extrapolated = Exchange(currency, to)
        val extrapolatedValue = exchangeRate1.toFloat().div(exchange2.toFloat())
        val formatted = formatToDP(value = extrapolatedValue)
        return extrapolated.rateCode() to formatted.toFloat()
    }

    private fun formatToDP(places: Int = 6, value: Float) = BigDecimal(value.toDouble())
        .setScale(places, RoundingMode.FLOOR)


}