package com.teovladusic.widgetsforstripe.core.data.model

import java.util.UUID

data class StripeProject(
    val id: String = UUID.randomUUID().toString(),
    val apiKey: String,
    val projectName: String
)
