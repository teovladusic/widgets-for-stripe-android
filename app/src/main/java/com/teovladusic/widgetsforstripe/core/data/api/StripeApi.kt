package com.teovladusic.widgetsforstripe.core.data.api

import com.teovladusic.widgetsforstripe.core.data.api_result.ApiResult
import com.teovladusic.widgetsforstripe.core.data.model.StripeChargesListResponse
import com.teovladusic.widgetsforstripe.core.data.model.StripeSubscriptionsListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StripeApi {

    @GET("charges")
    suspend fun fetchCharges(
        @Query("created[gte]") created: String? = null,
        @Query("limit") limit: Int = 100,
    ): ApiResult<StripeChargesListResponse>

    @GET("subscriptions")
    suspend fun fetchSubscriptions(
        @Query("limit") limit: Int = 100,
    ): ApiResult<StripeSubscriptionsListResponse>
}
