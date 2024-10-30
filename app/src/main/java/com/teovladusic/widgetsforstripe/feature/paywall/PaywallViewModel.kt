package com.teovladusic.widgetsforstripe.feature.paywall

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
//import com.android.billingclient.api.BillingFlowParams
//import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
//import com.teovladusic.widgetsforstripe.core.domain.billing.BillingClientWrapper
import com.teovladusic.widgetsforstripe.core.domain.model.paywall.PurchasableProduct
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    private val preferencesManager: PreferencesManager,
//    private val billingClientWrapper: BillingClientWrapper
) : ViewModel() {

//    private val _selectedProductId = MutableStateFlow<String?>(null)
//
//    init {
//        viewModelScope.launch {
//            if (preferencesManager.isPaywallShown.first()) {
//                _selectedProductId.update { "com.teovladusic.widgetsforstripe.annual" }
//            }
//        }
//    }
//
//    val productDetailsList =
//        combine(_selectedProductId, billingClientWrapper.productDetailsList) { selectedId, list ->
//            list.map { product ->
//                PurchasableProduct(
//                    name = product.name,
//                    price = product.oneTimePurchaseOfferDetails?.formattedPrice
//                        ?: product.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice
//                        ?: "",
//                    isSelected = selectedId == product.productId,
//                    isBought = false,
//                    discountPercent = if (product.productId == "com.teovladusic.widgetsforstripe.annual") 67 else null,
//                    id = product.productId
//                )
//            }
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
//            initialValue = emptyList()
//        )
//
//    fun executeAction(action: PaywallAction) {
//        when (action) {
//            PaywallAction.ClosePaywall -> closePaywall()
//            is PaywallAction.SelectProduct -> _selectedProductId.update { action.id }
//            is PaywallAction.BuyProduct -> buySelectedProduct(action.activity)
//        }
//    }
//
//    private fun buySelectedProduct(activity: Activity) {
//        val productId = _selectedProductId.value ?: return
//        val productDetails =
//            billingClientWrapper.productDetailsList.value.find { it.productId == productId }
//                ?: return
//
//        val offerToken =
//            if (productDetails.oneTimePurchaseOfferDetails != null) null else productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
//
//        val productDetailsParamsList = listOf(
//            BillingFlowParams.ProductDetailsParams.newBuilder()
//                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
//                .setProductDetails(productDetails)
//                // For One-time product, "setOfferToken" method shouldn't be called.
//                // For subscriptions, to get an offer token, call ProductDetails.subscriptionOfferDetails()
//                // for a list of offers that are available to the user
//                .apply { offerToken?.let { setOfferToken(it) } }
//                .build()
//        )
//
//        val params = BillingFlowParams.newBuilder()
//            .setProductDetailsParamsList(productDetailsParamsList)
//            .build()
//
//        billingClientWrapper.launchBillingFlow(
//            activity = activity,
//            params = params
//        )
//    }
//
//    private fun closePaywall() {
//        viewModelScope.launch {
//            val isPaywallShown = preferencesManager.isPaywallShown.first()
//            if (isPaywallShown) {
//                mainNavigator.navigateBack()
//            } else {
//                preferencesManager.setIsPaywallShown(isPaywallShown = true)
//                mainNavigator.navigateToStartDestination()
//            }
//        }
//    }
}

sealed interface PaywallAction {
    data object ClosePaywall : PaywallAction
    data class SelectProduct(val id: String) : PaywallAction
    data class BuyProduct(val activity: Activity) : PaywallAction
}
