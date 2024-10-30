package com.teovladusic.widgetsforstripe.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.teovladusic.widgetsforstripe.core.data.model.StripeProject
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithCharges
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithMrr
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

val Context.appDataStore: DataStore<Preferences> by preferencesDataStore("app_data_store")

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val gson = Gson()

    val isOnboarded: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKey.KEY_IS_ONBOARDED] ?: false
    }

    suspend fun setIsOnBoarded(isOnBoarded: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKey.KEY_IS_ONBOARDED] = isOnBoarded
        }
    }

    val isProjectAdded: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKey.KEY_IS_PROJECT_ADDED] ?: false
    }

    suspend fun setIsProjectAdded(isProjectAdded: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKey.KEY_IS_PROJECT_ADDED] = isProjectAdded
        }
    }

    val isReviewScreenShown: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKey.KEY_IS_REVIEW_SCREEN_SHOWN] ?: false
    }

    suspend fun setIsReviewScreenShown(isReviewScreenShown: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKey.KEY_IS_REVIEW_SCREEN_SHOWN] = isReviewScreenShown
        }
    }

    val isPaywallShown: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKey.KEY_IS_PAYWALL_SHOWN] ?: false
    }

    suspend fun setIsPaywallShown(isPaywallShown: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKey.KEY_IS_PAYWALL_SHOWN] = isPaywallShown
        }
    }

    val stripeProjects: Flow<List<StripeProject>> = context.appDataStore.data.map { preferences ->
        val json = preferences[PreferencesKey.KEY_STRIPE_PROJECTS]
        try {
            gson.fromJson(json, Array<StripeProject>::class.java).toList()
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    suspend fun addStripeProject(project: StripeProject) {
        val projects = stripeProjects.first().toMutableList()
        projects.add(project)
        val json = gson.toJson(projects)
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKey.KEY_STRIPE_PROJECTS] = json
        }
    }

    suspend fun removeStripeProject(id: String) {
        val projects = stripeProjects.first().toMutableList()
        projects.removeAll { it.id == id }
        val json = gson.toJson(projects)
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKey.KEY_STRIPE_PROJECTS] = json
        }
    }

    val selectedStripeProject: Flow<String?> = context.appDataStore.data.map { preferences ->
        preferences[PreferencesKey.KEY_SELECTED_STRIPE_PROJECT]
    }

    suspend fun selectStripeProject(id: String?) {
        context.appDataStore.edit { preferences ->
            if (id == null) {
                preferences.remove(PreferencesKey.KEY_SELECTED_STRIPE_PROJECT)
            } else {
                preferences[PreferencesKey.KEY_SELECTED_STRIPE_PROJECT] = id
            }
        }
    }

    val selectedStripeProjectWithCharges: Flow<StripeProjectWithCharges?> =
        context.appDataStore.data.map { preferences ->
            val json = preferences[PreferencesKey.KEY_SELECTED_STRIPE_PROJECT_WITH_CHARGES]
            try {
                gson.fromJson(json, StripeProjectWithCharges::class.java)
            } catch (e: Exception) {
                Timber.e(e)
                null
            }
        }

    suspend fun setStripeProjectWithCharges(stripeProjectWithCharges: StripeProjectWithCharges) {
        val json = gson.toJson(stripeProjectWithCharges)
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKey.KEY_SELECTED_STRIPE_PROJECT_WITH_CHARGES] = json
        }
    }

    val projectWithMrr: Flow<StripeProjectWithMrr?> = context.appDataStore.data.map { preferences ->
        val json = preferences[PreferencesKey.KEY_MRR]
        try {
            gson.fromJson(json, StripeProjectWithMrr::class.java)
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    suspend fun setProjectWithMrr(projectWithMrr: StripeProjectWithMrr) {
        val json = gson.toJson(projectWithMrr)
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKey.KEY_MRR] = json
        }
    }
}

private object PreferencesKey {
    val KEY_IS_ONBOARDED = booleanPreferencesKey("KEY_IS_ONBOARDED")
    val KEY_IS_PROJECT_ADDED = booleanPreferencesKey("KEY_IS_PROJECT_ADDED")
    val KEY_IS_REVIEW_SCREEN_SHOWN = booleanPreferencesKey("KEY_IS_REVIEW_SCREEN_SHOWN")
    val KEY_IS_PAYWALL_SHOWN = booleanPreferencesKey("KEY_IS_PAYWALL_SHOWN")
    val KEY_STRIPE_PROJECTS = stringPreferencesKey("KEY_STRIPE_PROJECTS")
    val KEY_SELECTED_STRIPE_PROJECT = stringPreferencesKey("KEY_SELECTED_STRIPE_PROJECT")
    val KEY_SELECTED_STRIPE_PROJECT_WITH_CHARGES =
        stringPreferencesKey("KEY_SELECTED_STRIPE_PROJECT_WITH_CHARGES")
    val KEY_MRR = stringPreferencesKey("KEY_MRR")
}
