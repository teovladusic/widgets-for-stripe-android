package com.teovladusic.widgetsforstripe.core.navigation.navigator

import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import com.teovladusic.widgetsforstripe.core.navigation.bottom_bar.BottomBarDestination
import com.teovladusic.widgetsforstripe.core.navigation.navigation_manager.RootNavigationManager
import com.teovladusic.widgetsforstripe.feature.NavGraphs
import com.teovladusic.widgetsforstripe.feature.destinations.AddProjectScreenDestination
import com.teovladusic.widgetsforstripe.feature.destinations.FinishOnBoardingScreenDestination
import com.teovladusic.widgetsforstripe.feature.destinations.HomeScreenDestination
import com.teovladusic.widgetsforstripe.feature.destinations.LeaveReviewScreenDestination
import com.teovladusic.widgetsforstripe.feature.destinations.OnBoardingScreenDestination
import com.teovladusic.widgetsforstripe.feature.destinations.PaywallScreenDestination
import com.teovladusic.widgetsforstripe.feature.destinations.SelectProjectScreenDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainNavigator @Inject constructor(
    val navigationManager: RootNavigationManager,
    private val preferencesManager: PreferencesManager
) {

    suspend fun navigateToOnboardingScreen() {
        navigationManager.navigateTo(route = OnBoardingScreenDestination().route)
    }

    suspend fun navigateToFinishOnBoardingScreen() {
        navigationManager.navigateTo(
            route = FinishOnBoardingScreenDestination().route
        )
    }

    suspend fun navigateToStartDestination() {
        withContext(Dispatchers.Default) {
            val isOnBoarded = preferencesManager.isOnboarded.first()
            if (!isOnBoarded) {
                return@withContext // start screen is welcome, so no need to navigate
            }

            val isProjectAdded = preferencesManager.isProjectAdded.first()
            if (!isProjectAdded) {
                navigationManager.navigateTo(
                    route = AddProjectScreenDestination().route,
                    popUpToRoute = NavGraphs.root.route,
                    inclusive = true
                )
                return@withContext
            }

            val isReviewScreenShown = preferencesManager.isReviewScreenShown.first()
            if (!isReviewScreenShown) {
                navigationManager.navigateTo(
                    route = LeaveReviewScreenDestination().route,
                    popUpToRoute = NavGraphs.root.route,
                    inclusive = true
                )
                return@withContext
            }

//            val isPaywallShown = preferencesManager.isPaywallShown.first()
//            if (!isPaywallShown) {
//                navigationManager.navigateTo(
//                    route = PaywallScreenDestination().route,
//                    popUpToRoute = NavGraphs.root.route,
//                    inclusive = true
//                )
//                return@withContext
//            }

            navigationManager.navigateTo(
                route = HomeScreenDestination().route,
                popUpToRoute = NavGraphs.root.route,
                inclusive = true
            )
        }
    }

    suspend fun navigateBack() {
        navigationManager.navigateBack()
    }

    suspend fun navigateToBottomBarDestination(destination: BottomBarDestination) {
        navigationManager.navigateTo(
            route = destination.route,
            popUpToRoute = "home_screen",
            inclusive = false,
            isSingleTop = true,
            savePopupToState = true,
            restoreState = true
        )
    }

    suspend fun navigateToSelectProject() {
        navigationManager.navigateTo(route = SelectProjectScreenDestination().route)
    }

    suspend fun navigateToPaywall() {
        navigationManager.navigateTo(route = PaywallScreenDestination().route)
    }

    suspend fun navigateToAddProject() {
        navigationManager.navigateTo(route = AddProjectScreenDestination().route)
    }
}
