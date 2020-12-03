package com.danijax.currencyconverter

import android.view.View
import com.danijax.currencyconverter.model.Currency
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


/**
 * Extension function on a long value representing a unix time stamp converting it to LocalDataTime
 * Object
 */
fun Long.toDateTime(): LocalDateTime = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(this),
    TimeZone.getDefault().toZoneId()
)

/**
 * Extension function on a LocalDateTime Object formating it to a human readable string
 */
fun LocalDateTime.formatFor(): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    return this.format(formatter)
}

/**
 * Extension function on a LocalDateTime Object converting it to  Long value representing a unix
 * time stamp
 * Object
 */
fun LocalDateTime.toTimeStamp() =
    ZonedDateTime.of(this, ZoneId.systemDefault()).toInstant().toEpochMilli()

/**
 * Extension function on a Currency Object to generate the url to get the Flag icon
 */
fun Currency.generateFlagUrl(): String {
    val iso2 = Code.substring(0, 2)
    return "http://www.geognos.com/api/en/countries/flag/$iso2.png"

}

/**
 * Extension function on a String Object representing a Floating point number and formating it in
 * exponential format e.g 100000000 can be written as 1E8
 */
fun String.formatExponentialForm(): String {
    return DecimalFormat("#.##E0").format(BigDecimal(this))
}

/**
 * Extension function on a View Object to to toggle it's visibility
 */
fun View.showOrHide(show: Boolean){
    visibility = if(show) View.VISIBLE
    else View.GONE

}



