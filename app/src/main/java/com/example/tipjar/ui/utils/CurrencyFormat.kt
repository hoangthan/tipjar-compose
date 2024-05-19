package com.example.tipjar.ui.utils

import java.util.Locale


fun Double.asCurrencyString(
    currencySign: String = "$",
    numberOfFractionDigits: Int = 2
): String {
    return String.format(Locale.getDefault(), "$currencySign%.${numberOfFractionDigits}f", this)
}
