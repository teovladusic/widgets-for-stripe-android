package com.teovladusic.widgetsforstripe.core.navigation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import com.teovladusic.widgetsforstripe.feature.NavGraphs

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
fun WidgetsForStripeNavigation(
    mainNavigator: MainNavigator,
    onCurrentRouteChanged: (NavDestination?) -> Unit = {}
) {
    val engine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING
    )
    val controller = engine.rememberNavController()
    val currentBackStackEntry by controller.currentBackStackEntryAsState()
    val insets = getInsets(route = currentBackStackEntry?.destination?.route)
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val bottomSheetNavigator = remember { BottomSheetNavigator(sheetState) }
    controller.navigatorProvider.addNavigator(bottomSheetNavigator)

    LaunchedEffect(key1 = currentBackStackEntry) {
        onCurrentRouteChanged(currentBackStackEntry?.destination)
    }

    NavigationEffects(
        navigationChannel = mainNavigator.navigationManager.navigationFlow,
        navHostController = controller
    )

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = defaultSheetShape,
        modifier = Modifier
            .windowInsetsPadding(insets)
            .fillMaxSize()
    ) {
        DestinationsNavHost(
            engine = engine,
            navController = controller,
            navGraph = NavGraphs.root,
            modifier = Modifier.fillMaxSize()
        )
    }
}

private val defaultSheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)

@Composable
fun getInsets(route: String?): WindowInsets {
    return when {
        fullScreenRoutes.contains(route) -> WindowInsets(left = 0, top = 0, right = 0, bottom = 0)
        else -> WindowInsets.safeDrawing
    }
}

private val fullScreenRoutes: List<String>
    get() = listOf()
