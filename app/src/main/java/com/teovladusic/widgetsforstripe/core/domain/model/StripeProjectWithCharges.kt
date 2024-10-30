package com.teovladusic.widgetsforstripe.core.domain.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class StripeProjectWithCharges(
    val id: String,
    val name: String,
    val charges: List<Charge>,
    val timeRefreshed: LocalDateTime?,
) {
    private val totalRevenueLast30Days: Long = getRevenueForLast30Days()
    val formattedTotalRevenueLast30Days = "%.2f".format(totalRevenueLast30Days / 100f)

    val timeRefreshedString: String? = timeRefreshed?.format(DateTimeFormatter.ofPattern("HH:mm"))

    val revenueForLast7Days: List<Pair<String, Long>> = getRevenueForLast7Days()
    private val totalRevenueLast7Days = revenueForLast7Days.sumOf { it.second }
    val formattedTotalRevenueLast7Days = "%.2f".format(totalRevenueLast7Days / 100f)

    val charges112DaysAgoAmount = getChargesAmount112DaysAgoList()
    val charges49DaysAgoAmount = getChargesAmount49DaysAgoList()
}

data class Charge(
    val id: String,
    val amount: Long,
    val created: LocalDateTime,
    val currency: String
)

fun StripeProjectWithCharges.getRevenueForLast7Days(): List<Pair<String, Long>> {
    // Initialize a map to hold revenue per day
    val dailyRevenueMap = mutableMapOf<LocalDateTime, Long>().apply {
        // Create entries for the last 7 days, initialized to 0
        for (i in 0..6) {
            val day = LocalDateTime.now().minusDays(i.toLong()).truncatedTo(ChronoUnit.DAYS)
            put(day, 0L)
        }
    }

    // Group charges by their creation day and sum the amounts
    charges.forEach { charge ->
        val chargeDate = charge.created.truncatedTo(ChronoUnit.DAYS)
        // Only consider charges from the last 7 days
        if (dailyRevenueMap.containsKey(chargeDate)) {
            dailyRevenueMap[chargeDate] = dailyRevenueMap[chargeDate]!! + charge.amount
        }
    }

    // Return a list of daily revenue pairs (date as String and total amount) for the graph
    return dailyRevenueMap
        .toSortedMap()  // Ensure the days are in order
        .map { (date, total) ->
            Pair(date.toLocalDate().toString(), total)
        }
}

fun StripeProjectWithCharges.getRevenueForLast30Days(): Long {
    val dateTime30DaysAgo = LocalDateTime.now().minusDays(30)
    return charges.filter { it.created.isAfter(dateTime30DaysAgo) }.sumOf { it.amount }
}

fun StripeProjectWithCharges.getChargesAmount112DaysAgoList(): List<Pair<Long, Color>> {
    val colorLevels = listOf(
        Color(0xFF0E0F1C),
        Color(0xFF3A3C6B),
        Color(0xFF4F527E),
        Color(0xFF4B51FD),
        Color(0xFF3036C8)
    )

    val list = mutableListOf<Long>()
    var day = LocalDateTime.now().minusDays(111).toLocalDate()

    for (i in 0 until 112) {
        val amount = charges.filter { it.created.toLocalDate() == day }.sumOf { it.amount }
        list.add(amount)
        day = day.plusDays(1)
    }

    val maxRevenue = list.maxOfOrNull { it } ?: 0L

    val getColorForRevenue: (Long) -> Color = { revenue ->
        when {
            revenue == 0L -> colorLevels[0]
            revenue <= maxRevenue * 0.25 -> colorLevels[1]
            revenue <= maxRevenue * 0.5 -> colorLevels[2]
            revenue <= maxRevenue * 0.75 -> colorLevels[3]
            else -> colorLevels[4]
        }
    }

    return list.map {
        it to getColorForRevenue(it)
    }
}

fun StripeProjectWithCharges.getChargesAmount49DaysAgoList(): List<Pair<Long, Color>> {
    val colorLevels = listOf(
        Color(0xFF0E0F1C),
        Color(0xFF3A3C6B),
        Color(0xFF4F527E),
        Color(0xFF4B51FD),
        Color(0xFF3036C8)
    )
    val list = mutableListOf<Long>()
    var day = LocalDateTime.now().minusDays(48).toLocalDate()

    for (i in 0 until 49) {
        val amount = charges.filter { it.created.toLocalDate() == day }.sumOf { it.amount }
        list.add(amount)
        day = day.plusDays(1)
    }

    val maxRevenue = list.maxOfOrNull { it } ?: 0L

    val getColorForRevenue: (Long) -> Color = { revenue ->
        when {
            revenue == 0L -> colorLevels[0]
            revenue <= maxRevenue * 0.25 -> colorLevels[1]
            revenue <= maxRevenue * 0.5 -> colorLevels[2]
            revenue <= maxRevenue * 0.75 -> colorLevels[3]
            else -> colorLevels[4]
        }
    }

    return list.map {
        it to getColorForRevenue(it)
    }
}

