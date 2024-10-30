package com.teovladusic.widgetsforstripe.feature.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import com.teovladusic.widgetsforstripe.core.navigation.bottom_bar.BottomBarDestination
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import com.teovladusic.widgetsforstripe.core.util.constants.AndroidIntentLauncher
import com.teovladusic.widgetsforstripe.core.util.constants.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state

    val selectedProjectName: Flow<String?> = combine(
        preferencesManager.selectedStripeProject,
        preferencesManager.stripeProjects
    ) { id, projects ->
        projects.find { it.id == id }?.projectName
    }

    fun executeAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.BottomBarDestinationClick -> navigateToBottomBarDestination(action.destination)
            SettingsAction.RateAppClick ->
                AndroidIntentLauncher.openUrl(context = context, url = AppConstants.PLAY_STORE_URL)

            SettingsAction.SelectProjectClick -> viewModelScope.launch {
                mainNavigator.navigateToSelectProject()
            }

            SettingsAction.SuggestNewFeaturesClick -> AndroidIntentLauncher.openSendEmailForm(
                context = context,
                mailTo = "teo.vladusic@gmail.com",
                subject = "New Feature Suggestion",
                body = "Hey Teo\n"
            )

            SettingsAction.SupportClick -> AndroidIntentLauncher.openSendEmailForm(
                context = context,
                mailTo = "teo.vladusic@gmail.com",
                subject = "Widgets For Stripe Support",
                body = "Hey Teo\n"
            )

            SettingsAction.UnlockPremiumFeaturesClick -> viewModelScope.launch {
                mainNavigator.navigateToPaywall()
            }
        }
    }

    private fun navigateToBottomBarDestination(destination: BottomBarDestination) {
        if (destination == BottomBarDestination.Settings) return
        viewModelScope.launch {
            mainNavigator.navigateToBottomBarDestination(destination)
        }
    }
}

data class SettingsUiState(
    val hasPremium: Boolean = false
)

sealed interface SettingsAction {
    data object UnlockPremiumFeaturesClick : SettingsAction
    data class BottomBarDestinationClick(val destination: BottomBarDestination) : SettingsAction
    data object SelectProjectClick : SettingsAction
    data object SuggestNewFeaturesClick : SettingsAction
    data object RateAppClick : SettingsAction
    data object SupportClick : SettingsAction
}
