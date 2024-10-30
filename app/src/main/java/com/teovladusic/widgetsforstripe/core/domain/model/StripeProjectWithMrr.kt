package com.teovladusic.widgetsforstripe.core.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class StripeProjectWithMrr(
    val id: String,
    val name: String,
    val mrr: Double,
    val timeRefreshed: LocalDateTime?,
) {
    val timeRefreshedString: String? = timeRefreshed?.format(DateTimeFormatter.ofPattern("HH:mm"))
    val formattedMrr: String = "%.2f".format(mrr)
}
