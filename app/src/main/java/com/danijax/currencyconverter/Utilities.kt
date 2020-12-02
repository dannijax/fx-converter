package com.danijax.currencyconverter

import android.util.Log
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

fun Long.toDateTime(): LocalDateTime = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(this),
    TimeZone.getDefault().toZoneId()
)


fun LocalDateTime.formatFor(): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    return this.format(formatter)
}

fun LocalDateTime.toTimeStamp() =
    ZonedDateTime.of(this, ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Currency.generateFlagUrl(): String {
    val iso2 = Code.substring(0, 2)
    return "http://www.geognos.com/api/en/countries/flag/$iso2.png"

}

fun String.formatExponentialForm(): String {
    return DecimalFormat("#.##E0").format(BigDecimal(this))
}

fun View.showOrHide(show: Boolean){
    if(show) visibility = View.VISIBLE
    else visibility = View.GONE

}



