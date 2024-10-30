package com.teovladusic.widgetsforstripe.core.navigation.navigation_manager

import com.teovladusic.widgetsforstripe.core.navigation.model.NavigationIntent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseNavigationManager {
    private val startDestination = "root"

    private val _navigationChannel = MutableSharedFlow<NavigationIntent>(
        replay = 1,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigationFlow: SharedFlow<NavigationIntent> = _navigationChannel

    suspend fun bottomNavigationNavigateTo(route: String) {
        _navigationChannel.emit(
            NavigationIntent.NavigateTo(
                route = route,
                popUpToRoute = startDestination,
                inclusive = false,
                isSingleTop = true,
                savePopupToState = true,
                restoreState = true
            )
        )
    }

    suspend fun navigateBack(route: String? = null, inclusive: Boolean = false) {
        _navigationChannel.emit(
            NavigationIntent.NavigateBack(
                route = route,
                inclusive = inclusive,
            ),
        )
    }

    suspend fun navigateTo(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = false,
        isSingleTop: Boolean = false,
        savePopupToState: Boolean = false,
        restoreState: Boolean = false
    ) {
        _navigationChannel.emit(
            NavigationIntent.NavigateTo(
                route = route,
                popUpToRoute = popUpToRoute,
                inclusive = inclusive,
                isSingleTop = isSingleTop,
                savePopupToState = savePopupToState,
                restoreState = restoreState
            )
        )
    }
}
