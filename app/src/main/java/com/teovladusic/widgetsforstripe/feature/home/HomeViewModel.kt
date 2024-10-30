package com.teovladusic.widgetsforstripe.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.widgetsforstripe.core.data.repository.StripeRepository
import com.teovladusic.widgetsforstripe.core.navigation.bottom_bar.BottomBarDestination
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    stripeRepository: StripeRepository,
) : ViewModel() {

    val stripeProject = stripeRepository.selectedStripeProjectWithCharges
    val projectWithMrr = stripeRepository.monthlyRepeatingRevenue

    fun executeAction(action: HomeAction) {
        when (action) {
            is HomeAction.BottomBarDestinationClick -> navigateToBottomBarDestination(action.destination)
        }
    }

    private fun navigateToBottomBarDestination(destination: BottomBarDestination) {
        if (destination == BottomBarDestination.Home) return
        viewModelScope.launch {
            mainNavigator.navigateToBottomBarDestination(destination)
        }
    }
}

sealed interface HomeAction {
    data class BottomBarDestinationClick(val destination: BottomBarDestination) : HomeAction
}
