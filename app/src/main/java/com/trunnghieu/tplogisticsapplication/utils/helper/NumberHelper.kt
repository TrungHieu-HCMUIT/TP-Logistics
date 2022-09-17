package com.trunnghieu.tplogisticsapplication.utils.helper

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

object NumberHelper {

    private var defNumAfterDecimal = 0

    fun setDefaultNumberAfterDecimal(numAfterDecimal: Int) {
        defNumAfterDecimal = numAfterDecimal
    }

    /**
     * Number with decimal format
     *
     *
     *
     * ●	Use comma (,) for thousand delimiter.
     * ●	Use dot (.) for decimal delimiter.
     * ●	Ex : 123,456.789
     * ●	Right align.
     *
     *
     * @param number
     * @return
     */
    @JvmStatic
    fun decimalFormat(number: Double): String {
        return String.format("%s", decimalFormal(defNumAfterDecimal).format(number))
    }

    @JvmStatic
    fun decimalFormat(number: Double, numOfDecimal: Int): String {
        return String.format("%s", decimalFormal(numOfDecimal).format(number))
    }

    private fun decimalFormal(numOfDecimal: Int): NumberFormat {
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        val formatSymbols = DecimalFormatSymbols()

        // Use dot (.) for decimal delimiter.
        formatSymbols.decimalSeparator = '.'
        // Use comma (,) for thousand delimiter (if it's over 1000).
        formatSymbols.groupingSeparator = ','
        // Apply symbol format
        (numberFormat as DecimalFormat).decimalFormatSymbols = formatSymbols
        numberFormat.setGroupingUsed(true)

        // Set fixed decimal.
        numberFormat.setMinimumFractionDigits(numOfDecimal)
        numberFormat.setMaximumFractionDigits(numOfDecimal)
        return numberFormat
    }
}