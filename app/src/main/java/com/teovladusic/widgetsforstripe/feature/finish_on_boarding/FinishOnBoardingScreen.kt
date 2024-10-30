package com.teovladusic.widgetsforstripe.feature.finish_on_boarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.components.WFSPrimaryButton
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens

@Destination
@Composable
fun FinishOnBoardingScreen(viewModel: FinishOnBoardingViewModel = hiltViewModel()) {
    Content { viewModel.onFinishClick() }
}

@Composable
private fun Content(finishClick: () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Black) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Banner()
            Spacer(modifier = Modifier.height(Dimens.spacing_m))
            FinishButton(onClick = finishClick)
        }
    }
}

@Composable
private fun FinishButton(onClick: () -> Unit) {
    WFSPrimaryButton(
        modifier = Modifier
            .padding(horizontal = Dimens.spacing_m)
            .padding(bottom = Dimens.spacing_m)
            .fillMaxWidth(),
        text = stringResource(R.string.lets_go)
    ) { onClick() }
}

@Composable
private fun ColumnScope.Banner() {
    Image(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        painter = painterResource(R.drawable.img_widgets_on_us_banner),
        contentDescription = null,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    Content() {}
}
