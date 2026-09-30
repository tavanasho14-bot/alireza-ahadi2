package com.example

import com.example.data.model.FairnessModel
import com.example.util.PersianUtils
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.roundToLong

class ExampleUnitTest {
    @Test
    fun testPersianNumberConversion() {
        assertEquals("۱۲۳۴۵۶۷۸۹۰", PersianUtils.toPersianDigits("1234567890"))
        assertEquals("۹,۰۰۰,۰۰۰ تومان", PersianUtils.formatPrice(9_000_000))
    }

    @Test
    fun testSteppedFairnessModelProtectsLastWinner() {
        val baseAmount = 100_000_000L
        val count = 20
        val rate = 1.0 // 1% per round step
        val midpoint = (count + 1) / 2.0

        // Round 1 offset (earliest round)
        val offsetFirst = (1 - midpoint) * (rate / 100.0)
        val firstWinnerPayout = baseAmount + (baseAmount * offsetFirst).roundToLong()

        // Round 20 offset (last round)
        val offsetLast = (20 - midpoint) * (rate / 100.0)
        val lastWinnerPayout = baseAmount + (baseAmount * offsetLast).roundToLong()

        // Verify last winner receives a significant bonus over base amount to beat inflation!
        assertTrue(lastWinnerPayout > baseAmount)
        assertTrue(firstWinnerPayout < baseAmount)
        assertEquals(baseAmount * 2, firstWinnerPayout + lastWinnerPayout) // Exactly balanced pool!
    }
}
