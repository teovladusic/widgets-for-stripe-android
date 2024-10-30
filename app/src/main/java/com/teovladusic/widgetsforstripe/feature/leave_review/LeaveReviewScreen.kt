package com.teovladusic.widgetsforstripe.feature.leave_review

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.components.WFSPrimaryButton
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.util.extension.findActivity

@Destination
@Composable
fun LeaveReviewScreen(viewModel: LeaveReviewViewModel = hiltViewModel()) {
    val activity = LocalContext.current.findActivity()
    Content(onContinueClick = { viewModel.onContinueClick(activity) })
}

@Composable
private fun Content(onContinueClick: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(color = 0xFF1E1E1B)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spacing_m),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.help_us_grow),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = NunitoFontFamily
                )
                Spacer(Modifier.height(Dimens.spacing_m))
                Image(
                    painter = painterResource(R.drawable.img_hearth),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(fraction = .7f),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(Modifier.height(Dimens.spacing_m))
                Text(
                    text = stringResource(R.string.leave_review_desc),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontFamily = NunitoFontFamily,
                    modifier = Modifier.padding(horizontal = Dimens.spacing_xs)
                )
            }
            WFSPrimaryButton(
                text = stringResource(R.string.continue_label),
                modifier = Modifier
                    .padding(Dimens.spacing_m)
                    .fillMaxWidth(),
                onClick = onContinueClick
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Content() {}
}