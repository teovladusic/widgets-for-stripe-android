package com.teovladusic.widgetsforstripe.core.design_system.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Dimensions(
    val spacing_xxs: Dp,
    val spacing_xs: Dp,
    val spacing_s: Dp,
    val spacing_m: Dp,
    val spacing_l: Dp,
    val spacing_xl: Dp,
    val spacing_xxl: Dp,
    val minimum_touch_target: Dp = 48.dp,
)

val normalDimensions = Dimensions(
    spacing_xxs = 4.dp,
    spacing_xs = 8.dp,
    spacing_s = 12.dp,
    spacing_m = 16.dp,
    spacing_l = 24.dp,
    spacing_xl = 32.dp,
    spacing_xxl = 40.dp
)
