package com.teovladusic.widgetsforstripe.core.navigation.model

sealed class NavigationIntent {
    data class NavigateBack(
        val route: String? = null,
        val inclusive: Boolean = false,
    ) : NavigationIntent()

    data class NavigateTo(
        val route: String,
        val popUpToRoute: String? = null,
        val inclusive: Boolean = false,
        val isSingleTop: Boolean = false,
        val savePopupToState: Boolean = false,
        val restoreState: Boolean = false
    ) : NavigationIntent()
}
