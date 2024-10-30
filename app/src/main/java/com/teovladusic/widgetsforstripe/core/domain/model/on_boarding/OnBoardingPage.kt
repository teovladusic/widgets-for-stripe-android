package com.teovladusic.widgetsforstripe.core.domain.model.on_boarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.teovladusic.widgetsforstripe.R

enum class OnBoardingPage {
    Revenue, GridGraph, Chart, Mrr
}

val OnBoardingPage.image: Painter
    @Composable get() = when (this) {
        OnBoardingPage.Revenue -> painterResource(R.drawable.img_revenue_widget)
        OnBoardingPage.GridGraph -> painterResource(R.drawable.img_grid_widget)
        OnBoardingPage.Chart -> painterResource(R.drawable.img_chart_widget)
        OnBoardingPage.Mrr -> painterResource(R.drawable.img_mrr_widget)
    }

val OnBoardingPage.title: String
    @Composable get() = when (this) {
        OnBoardingPage.Revenue -> stringResource(R.string.revenue_widget)
        OnBoardingPage.GridGraph -> stringResource(R.string.grid_graph_widget)
        OnBoardingPage.Chart -> stringResource(R.string.chart_widgets)
        OnBoardingPage.Mrr -> stringResource(R.string.mrr_widgets)
    }

val OnBoardingPage.description: String
    @Composable get() = when (this) {
        OnBoardingPage.Revenue -> stringResource(R.string.revenue_widget_desc)
        OnBoardingPage.GridGraph -> stringResource(R.string.grid_graph_widget_desc)
        OnBoardingPage.Chart -> stringResource(R.string.chart_widgets_desc)
        OnBoardingPage.Mrr -> stringResource(R.string.mrr_widgets_desc)
    }
