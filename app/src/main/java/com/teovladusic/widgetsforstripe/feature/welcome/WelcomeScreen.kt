package com.teovladusic.widgetsforstripe.feature.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.components.WFSPrimaryButton
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens

@RootNavGraph(start = true)
@Destination
@Composable
fun WelcomeScreen(viewModel: WelcomeScreenViewModel = hiltViewModel()) {
    Content { viewModel.onStartClick() }
}

@Composable
private fun Content(
    onStartClick: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Black) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Banner()
            StartButton(onClick = onStartClick)
        }
    }
}

@Composable
private fun ColumnScope.Banner() {
    Image(
        painter = painterResource(R.drawable.img_welcome_banner),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    )
}

@Composable
private fun StartButton(onClick: () -> Unit) {
    WFSPrimaryButton(
        modifier = Modifier
            .padding(horizontal = Dimens.spacing_m)
            .padding(bottom = Dimens.spacing_m)
            .fillMaxWidth(),
        text = stringResource(R.string.start)
    ) { onClick() }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Content {}
}
