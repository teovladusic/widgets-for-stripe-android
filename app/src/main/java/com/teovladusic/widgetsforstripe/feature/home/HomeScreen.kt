package com.teovladusic.widgetsforstripe.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithCharges
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithMrr
import com.teovladusic.widgetsforstripe.core.navigation.bottom_bar.BottomBar
import com.teovladusic.widgetsforstripe.feature.home.components.HomeChartWidget
import com.teovladusic.widgetsforstripe.feature.home.components.HomeGridGraphWidget
import com.teovladusic.widgetsforstripe.feature.home.components.HomeGridGraphWidgetSize
import com.teovladusic.widgetsforstripe.feature.home.components.HomeMrrWidget
import com.teovladusic.widgetsforstripe.feature.home.components.HomeRevenueWidget

@Destination
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val stripeProject by viewModel.stripeProject.collectAsStateWithLifecycle()
    val mrrProject by viewModel.projectWithMrr.collectAsStateWithLifecycle()

    Content(
        stripeProject = stripeProject,
        executeAction = viewModel::executeAction,
        projectWithMrr = mrrProject
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    stripeProject: StripeProjectWithCharges?,
    projectWithMrr: StripeProjectWithMrr?,
    executeAction: (HomeAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Color.Black,
        topBar = { Toolbar(scrollBehavior = scrollBehavior) },
        bottomBar = {
            BottomBar(selectedDestination = "home_screen") {
                executeAction(HomeAction.BottomBarDestinationClick(it))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item { SectionTitle(text = stringResource(R.string.revenue), isPremium = false) }
            item { RevenueWidgetsRow(stripeProject = stripeProject) }

            item { SectionTitle(text = stringResource(R.string.grid_graph), isPremium = true) }
            item { GridGraphWidgetsRow(stripeProject = stripeProject) }

            item { SectionTitle(text = stringResource(R.string.charts), isPremium = true) }
            item { ChartsWidgetsRow(stripeProject = stripeProject) }

            item { SectionTitle(text = stringResource(R.string.mrr), isPremium = true) }
            item { HomeMrrWidget(project = projectWithMrr) }
        }
    }
}

@Composable
private fun ChartsWidgetsRow(stripeProject: StripeProjectWithCharges?) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(all = Dimens.spacing_m),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacing_l)
    ) {
        item { HomeChartWidget(projectWithCharges = stripeProject, width = 220.dp) }
        item { HomeChartWidget(projectWithCharges = stripeProject, width = 300.dp) }
    }
}

@Composable
private fun GridGraphWidgetsRow(stripeProject: StripeProjectWithCharges?) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(all = Dimens.spacing_m),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacing_l)
    ) {
        item {
            HomeGridGraphWidget(
                project = stripeProject,
                size = HomeGridGraphWidgetSize.Small
            )
        }
        item {
            HomeGridGraphWidget(
                project = stripeProject,
                size = HomeGridGraphWidgetSize.Large
            )
        }
    }
}

@Composable
private fun RevenueWidgetsRow(stripeProject: StripeProjectWithCharges?) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(all = Dimens.spacing_m),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacing_l)
    ) {
        item { HomeRevenueWidget(project = stripeProject, width = 220.dp) }
        item { HomeRevenueWidget(project = stripeProject, width = 300.dp) }
    }
}

@Composable
private fun SectionTitle(text: String, isPremium: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spacing_m),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Bold
        )
        if (isPremium) {
            Spacer(Modifier.width(2.dp))
            Image(
                painter = painterResource(R.drawable.img_crown),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(R.string.premium),
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(scrollBehavior: TopAppBarScrollBehavior) {
    MediumTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.widgets_for_stripe),
                color = Primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {},
        actions = {},
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Black,
            scrolledContainerColor = Color.Black
        )
    )
}

@Preview
@Composable
private fun Preview() {
    Content(
        stripeProject = null,
        projectWithMrr = null,
        executeAction = {}
    )
}
