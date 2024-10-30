package com.teovladusic.widgetsforstripe.core.domain.model.paywall

data class PurchasableProduct(
    val id: String,
    val name: String,
    val price: String,
    val isSelected: Boolean,
    val isBought: Boolean,
    val discountPercent: Int?
)
