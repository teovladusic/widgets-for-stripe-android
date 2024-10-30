package com.teovladusic.widgetsforstripe.core.data.model

data class StripeChargeResponse(
    val id: String,
    val amount: Long,
    val created: Long,
    val currency: String,
)
