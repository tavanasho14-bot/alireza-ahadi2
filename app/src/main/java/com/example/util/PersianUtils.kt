package com.example.util

import java.text.DecimalFormat

object PersianUtils {
    private val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

    fun toPersianDigits(input: String): String {
        val sb = StringBuilder()
        for (ch in input) {
            if (ch in '0'..'9') {
                sb.append(persianDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun toPersianDigits(number: Long): String {
        return toPersianDigits(number.toString())
    }

    fun toPersianDigits(number: Int): String {
        return toPersianDigits(number.toString())
    }

    fun formatPrice(amount: Long, includeToman: Boolean = true): String {
        val formatter = DecimalFormat("#,###")
        val formatted = formatter.format(amount)
        val persian = toPersianDigits(formatted)
        return if (includeToman) "$persian تومان" else persian
    }

    fun formatPercent(percent: Int): String {
        return "${toPersianDigits(percent)}٪"
    }

    val jalaliMonths = listOf(
        "فروردین", "اردیبهشت", "خرداد",
        "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر",
        "دی", "بهمن", "اسفند"
    )

    fun getMonthName(monthIndex: Int): String {
        val normalized = ((monthIndex - 1) % 12 + 12) % 12
        return jalaliMonths[normalized]
    }
}
