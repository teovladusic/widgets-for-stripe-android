package com.teovladusic.widgetsforstripe.core.domain.billing

//import android.app.Activity
//import android.content.Context
//import com.android.billingclient.api.BillingClient
//import com.android.billingclient.api.BillingClientStateListener
//import com.android.billingclient.api.BillingFlowParams
//import com.android.billingclient.api.BillingResult
//import com.android.billingclient.api.PendingPurchasesParams
//import com.android.billingclient.api.ProductDetails
//import com.android.billingclient.api.ProductDetailsResponseListener
//import com.android.billingclient.api.Purchase
//import com.android.billingclient.api.PurchasesUpdatedListener
//import com.android.billingclient.api.QueryProductDetailsParams
//import com.android.billingclient.api.QueryPurchaseHistoryParams
//import com.android.billingclient.api.queryPurchaseHistory
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import timber.log.Timber
//import javax.inject.Inject
//import javax.inject.Singleton

///**
// * The [BillingClientWrapper] isolates the Google Play Billing's [BillingClient] methods needed
// * to have a simple implementation and emits responses to the data repository for processing.
// */
//@Singleton
//class BillingClientWrapper @Inject constructor(
//    @ApplicationContext context: Context,
//    private val coroutineScope: CoroutineScope
//) : PurchasesUpdatedListener {
//
//    var purchaseUpdatedListener: GooglePlayPurchaseListener? = null
//
//    // New Subscription ProductDetails
//    private val _productDetailsList = MutableStateFlow<List<ProductDetails>>(emptyList())
//    val productDetailsList = _productDetailsList.asStateFlow()
//
//    private val _hasPremium = MutableStateFlow<Boolean>(false)
//    val hasPremium = _hasPremium.asStateFlow()
//
//    private val billingClient = BillingClient.newBuilder(context)
//        .setListener(this)
//        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
//        .build()
//
//    fun startBillingConnection(billingConnectionState: MutableStateFlow<Boolean>) {
//        billingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingSetupFinished(billingResult: BillingResult) {
//                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                    Timber.d("Billing response OK")
//                    queryProductDetails(
//                        list = SUBSCRIPTION_IDS,
//                        type = BillingClient.ProductType.SUBS
//                    )
//                    queryProductDetails(
//                        list = PRODUCT_IDS,
//                        type = BillingClient.ProductType.INAPP
//                    )
//                    queryPurchasedProducts()
//                    billingConnectionState.update { true }
//                } else {
//                    Timber.d(billingResult.debugMessage)
//                }
//            }
//
//            override fun onBillingServiceDisconnected() {
//                Timber.e("Billing connection disconnected")
//                startBillingConnection(billingConnectionState)
//            }
//        })
//    }
//
//    fun queryPurchasedProducts() {
//        coroutineScope.launch {
//            val subsParams = QueryPurchaseHistoryParams.newBuilder()
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build()
//
//            val inAppParams = QueryPurchaseHistoryParams.newBuilder()
//                .setProductType(BillingClient.ProductType.INAPP)
//                .build()
//
//            val subs = billingClient.queryPurchaseHistory(params = subsParams)
//            val inApp = billingClient.queryPurchaseHistory(params = inAppParams)
//
//            val hasPremium = subs.purchaseHistoryRecordList.orEmpty()
//                .isNotEmpty() || inApp.purchaseHistoryRecordList.orEmpty().isNotEmpty()
//
//            _hasPremium.update { hasPremium }
//        }
//    }
//
//    private fun queryProductDetails(list: List<String>, type: String) {
//        val params = QueryProductDetailsParams.newBuilder()
//        val productList = mutableListOf<QueryProductDetailsParams.Product>()
//
//        for (product in list) {
//            productList.add(
//                QueryProductDetailsParams.Product.newBuilder()
//                    .setProductId(product)
//                    .setProductType(type)
//                    .build()
//            )
//        }
//        params.setProductList(productList)
//
//        billingClient.queryProductDetailsAsync(params.build()) { p0, p1 ->
//            this@BillingClientWrapper.onProductDetailsResponse(p0, p1)
//        }
//    }
//
//    /**
//    // [ProductDetailsResponseListener] implementation
//    // Listen to response back from [queryProductDetails] and emits the results
//    // to [_productDetailsList].
//     */
//    private fun onProductDetailsResponse(
//        billingResult: BillingResult,
//        productDetailsList: MutableList<ProductDetails>
//    ) {
//        val responseCode = billingResult.responseCode
//        val debugMessage = billingResult.debugMessage
//
//        when (responseCode) {
//            BillingClient.BillingResponseCode.OK -> {
//                if (productDetailsList.isEmpty()) {
//                    Timber.e(
//                        "onProductDetailsResponse: " +
//                                "Found null or empty ProductDetails. " +
//                                "Check to see if the Products you requested are correctly " +
//                                "published in the Google Play Console.",
//                    )
//                }
//
//                _productDetailsList.update {
//                    val list = it.toMutableList()
//
//                    productDetailsList.forEach { product ->
//                        val index =
//                            list.indexOfFirst { item -> item.productId == product.productId }
//
//                        if (index == -1) {
//                            list.add(product)
//                        } else {
//                            list[index] = product
//                        }
//                    }
//
//                    list
//                }
//            }
//
//            else -> {
//                Timber.e("onProductDetailsResponse: $responseCode $debugMessage")
//            }
//        }
//    }
//
//    fun launchBillingFlow(activity: Activity, params: BillingFlowParams) {
//        if (!billingClient.isReady) {
//            Timber.e("launchBillingFlow: BillingClient is not ready")
//        }
//        billingClient.launchBillingFlow(activity, params)
//    }
//
//    override fun onPurchasesUpdated(
//        billingResult: BillingResult,
//        purchases: List<Purchase>?
//    ) {
//        val responseCode = billingResult.responseCode
//        queryPurchasedProducts()
//
//        if (responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
//            purchaseUpdatedListener?.onGooglePlayPurchase(purchases)
//        } else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
//            Timber.e("User has cancelled")
//            purchaseUpdatedListener?.onGooglePlayPurchaseUserCancelled()
//        } else {
//            purchaseUpdatedListener?.onGooglePlayPurchaseError()
//            Timber.e("Purchase Updated, $responseCode ${billingResult.debugMessage}")
//        }
//    }
//
//    fun terminateBillingConnection() {
//        billingClient.endConnection()
//    }
//
//    companion object {
//        private val PRODUCT_IDS = listOf("com.teovladusic.widgetsforstripe.premium_lifetime")
//        private val SUBSCRIPTION_IDS = listOf(
//            "com.teovladusic.widgetsforstripe.annual",
//            "com.teovladusic.widgetsforstripe.monthly"
//        )
//    }
//}
