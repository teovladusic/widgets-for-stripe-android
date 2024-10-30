package com.teovladusic.widgetsforstripe.core.data.model

data class StripeSubscriptionResponse(
    val id: String,
    val plan: StripeSubscriptionPlanResponse
)

data class StripeSubscriptionPlanResponse(
    val active: Boolean,
    val interval: String,
    val amount: Long,
    val interval_count: Int
)
