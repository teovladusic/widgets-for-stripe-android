package com.teovladusic.widgetsforstripe.feature.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.components.WFSPrimaryButton
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary
import com.teovladusic.widgetsforstripe.core.domain.model.on_boarding.OnBoardingPage
import com.teovladusic.widgetsforstripe.core.domain.model.on_boarding.description
import com.teovladusic.widgetsforstripe.core.domain.model.on_boarding.image
import com.teovladusic.widgetsforstripe.core.domain.model.on_boarding.title
import kotlinx.coroutines.launch

@Destination
@Composable
fun OnBoardingScreen(viewModel: OnBoardingViewModel = hiltViewModel()) {
    Content(
        onContinueClick = {
            viewModel.onContinueClick()
        }
    )
}

@Composable
private fun Content(onContinueClick: () -> Unit) {
    val pagerState = rememberPagerState { OnBoardingPage.entries.size }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnBoardingPager(state = pagerState)
            Spacer(Modifier.height(Dimens.spacing_xxl))
            ContinueButton(
                onClick = {
                    if (pagerState.currentPage == OnBoardingPage.entries.lastIndex) {
                        onContinueClick()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ContinueButton(onClick: () -> Unit) {
    WFSPrimaryButton(
        modifier = Modifier
            .padding(horizontal = Dimens.spacing_m)
            .padding(bottom = Dimens.spacing_m)
            .fillMaxWidth(),
        text = stringResource(R.string.continue_label),
        onClick = onClick,
    )
}

@Composable
private fun ColumnScope.OnBoardingPager(state: PagerState) {
    HorizontalPager(
        state = state, modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) { page ->
        OnBoardingPagerItem(page = OnBoardingPage.entries[page])
    }
    Spacer(Modifier.height(Dimens.spacing_m))
    PageIndicator(state = state)
}

@Composable
private fun PageIndicator(state: PagerState) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(OnBoardingPage.entries.size) { i ->
            val color by animateColorAsState(
                targetValue = if (state.currentPage == i) Primary else Primary.copy(alpha = .1f),
                label = "color_pager_indicator_anim"
            )
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}

@Composable
private fun OnBoardingPagerItem(page: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spacing_m),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(fraction = .6f),
            painter = page.image,
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Spacer(Modifier.height(Dimens.spacing_s))
        Text(
            text = page.title,
            color = Primary,
            fontSize = 22.sp,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(Dimens.spacing_m))
        Text(
            text = page.description,
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = NunitoFontFamily,
            modifier = Modifier.padding(horizontal = Dimens.spacing_xs)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    Content {}
}
