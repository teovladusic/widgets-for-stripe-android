package com.teovladusic.widgetsforstripe.core.navigation.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.teovladusic.widgetsforstripe.core.navigation.model.NavigationIntent
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun NavigationEffects(
    navigationChannel: SharedFlow<NavigationIntent>,
    navHostController: NavHostController,
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }

            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }

                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        restoreState = false

                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) {
                                inclusive = intent.inclusive
                                saveState = intent.savePopupToState
                            }
                        }
                    }
                }
            }
        }
    }
}
