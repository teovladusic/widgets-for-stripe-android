package com.teovladusic.widgetsforstripe.core.data.repository

import android.content.Context
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithCharges
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithMrr
import kotlinx.coroutines.flow.Flow

class GlanceWidgetRepository(context: Context) {

    private val preferencesManager = PreferencesManager(context)

    val selectedProject: Flow<StripeProjectWithCharges?> =
        preferencesManager.selectedStripeProjectWithCharges

    val projectWithMrr: Flow<StripeProjectWithMrr?> =
        preferencesManager.projectWithMrr

    companion object {
        fun getInstance(context: Context): GlanceWidgetRepository {
            return GlanceWidgetRepository(context)
        }
    }
}
