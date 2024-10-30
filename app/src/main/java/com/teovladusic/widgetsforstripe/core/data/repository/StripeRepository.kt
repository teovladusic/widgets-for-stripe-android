package com.teovladusic.widgetsforstripe.core.data.repository

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.teovladusic.widgetsforstripe.core.data.api.StripeApi
import com.teovladusic.widgetsforstripe.core.data.model.StripeProject
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import com.teovladusic.widgetsforstripe.core.domain.model.Charge
import com.teovladusic.widgetsforstripe.core.domain.model.Result
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithCharges
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithMrr
import com.teovladusic.widgetsforstripe.core.domain.model.handleErrorAndMapSuccess
import com.teovladusic.widgetsforstripe.core.ui.widget.repeating_revenue.StripeMonthlyRepeatingRevenueWidget
import com.teovladusic.widgetsforstripe.core.ui.widget.revenue.Stripe7DaysRevenueGraphWidget
import com.teovladusic.widgetsforstripe.core.ui.widget.revenue.StripeRevenueLargeGridGraphWidget
import com.teovladusic.widgetsforstripe.core.ui.widget.revenue.StripeRevenueSmallGridGraphWidget
import com.teovladusic.widgetsforstripe.core.ui.widget.revenue.StripeRevenueWidget
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StripeRepository @Inject constructor(
    private val api: StripeApi,
    private val preferencesManager: PreferencesManager,
    coroutineScope: CoroutineScope,
    @ApplicationContext private val context: Context
) {
    val selectedStripeProjectWithCharges: StateFlow<StripeProjectWithCharges?> = combine(
        preferencesManager.selectedStripeProject,
        preferencesManager.stripeProjects
    ) { id, projects ->
        val project = getSelectedStripeProjectWithCharges(id = id, projects = projects).also {
            it?.let { updateWidgetsAndSaveLocal(project = it) }
        }

        project
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val monthlyRepeatingRevenue: StateFlow<StripeProjectWithMrr?> = combine(
        preferencesManager.selectedStripeProject,
        preferencesManager.stripeProjects
    ) { id, projects ->
        val projectWithMrr = getProjectWithMrr(id = id, projects = projects).also {
            it?.let { updateMRRWidgetAndSaveLocal(projectWithMrr = it) }
        }
        projectWithMrr
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    suspend fun getProjectWithMrr(
        id: String?,
        projects: List<StripeProject>
    ): StripeProjectWithMrr? {
        return withContext(Dispatchers.Default) {
            val selectedProject = projects.find { it.id == id } ?: return@withContext null

            val result = api.fetchSubscriptions().handleErrorAndMapSuccess { response ->
                response.data?.data.orEmpty()
            }

            if (result !is Result.Success) return@withContext null

            val mrr = result.value
                .filter { it.plan.active }
                .sumOf { subscription ->
                    val monthlyAmount: Double = when (subscription.plan.interval) {
                        "year" -> subscription.plan.amount / (12 * subscription.plan.interval_count).toDouble()
                        "month" -> subscription.plan.amount / subscription.plan.interval_count.toDouble()
                        "week" -> subscription.plan.amount * 4.34524 / subscription.plan.interval_count
                        "day" -> subscription.plan.amount * 30.4368 / subscription.plan.interval_count
                        else -> 0.0
                    }
                    monthlyAmount / 100.0
                }

            StripeProjectWithMrr(
                id = selectedProject.id,
                name = selectedProject.projectName,
                mrr = mrr,
                timeRefreshed = LocalDateTime.now(),
            )
        }
    }

    suspend fun getSelectedStripeProjectWithCharges(
        id: String?,
        projects: List<StripeProject>
    ): StripeProjectWithCharges? {
        return withContext(Dispatchers.Default) {
            val selectedProject = projects.find { it.id == id } ?: return@withContext null

            // 16 * 7 as its used by grid graph and that's it size
            val createAfterFilter =
                LocalDateTime.now().minusDays(16 * 7).toEpochSecond(ZoneOffset.UTC)

            val result = api.fetchCharges(created = createAfterFilter.toString())
                .handleErrorAndMapSuccess { response ->
                    response.data?.data.orEmpty()
                }

            if (result !is Result.Success) return@withContext null

            val charges = result.value.map {
                Charge(
                    id = it.id,
                    amount = it.amount,
                    created = LocalDateTime.ofEpochSecond(it.created, 0, ZoneOffset.UTC),
                    currency = it.currency
                )
            }

            StripeProjectWithCharges(
                id = selectedProject.id,
                name = selectedProject.projectName,
                charges = charges,
                timeRefreshed = LocalDateTime.now(),
            )
        }
    }

    suspend fun updateWidgetsAndSaveLocal(project: StripeProjectWithCharges): StripeProjectWithCharges {
        return project.also {
            preferencesManager.setStripeProjectWithCharges(it)
            withContext(Dispatchers.Main) {
                StripeRevenueWidget().updateAll(context)
                Stripe7DaysRevenueGraphWidget().updateAll(context)
                StripeRevenueLargeGridGraphWidget().updateAll(context)
                StripeRevenueSmallGridGraphWidget().updateAll(context)
            }
        }
    }

    suspend fun updateMRRWidgetAndSaveLocal(projectWithMrr: StripeProjectWithMrr) {
        withContext(Dispatchers.Default) {
            preferencesManager.setProjectWithMrr(projectWithMrr)
            StripeMonthlyRepeatingRevenueWidget().updateAll(context)
        }
    }
}
