package com.teovladusic.widgetsforstripe.feature.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.teovladusic.widgetsforstripe.BuildConfig
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary
import com.teovladusic.widgetsforstripe.core.navigation.bottom_bar.BottomBar
import com.teovladusic.widgetsforstripe.core.navigation.bottom_bar.BottomBarDestination
import com.teovladusic.widgetsforstripe.core.util.constants.AndroidIntentLauncher
import com.teovladusic.widgetsforstripe.core.util.constants.AppConstants

@Destination
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val selectedProjectName by viewModel.selectedProjectName.collectAsStateWithLifecycle(
        initialValue = null
    )
    Content(
        state = state,
        selectedProjectName = selectedProjectName,
        executeAction = viewModel::executeAction
    )
}

@Composable
private fun Content(
    state: SettingsUiState,
    selectedProjectName: String?,
    executeAction: (SettingsAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        bottomBar = {
            BottomBar(
                selectedDestination = BottomBarDestination.Settings.route,
                onClick = { executeAction(SettingsAction.BottomBarDestinationClick(it)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.spacing_m)
        ) {
            Spacer(Modifier.height(Dimens.spacing_m))
            SectionTitle(title = stringResource(R.string.stripe_projects))
            Spacer(Modifier.height(Dimens.spacing_s))
            StripeProject(
                onClick = { executeAction(SettingsAction.SelectProjectClick) },
                projectName = selectedProjectName
            )
            Spacer(Modifier.height(Dimens.spacing_xl))
            Info(executeAction = executeAction)
            AppVersion()
            Maker()
        }
    }
}

@Composable
private fun Maker() {
    val context = LocalContext.current
    Text(
        text = buildAnnotatedString {
            append("Made with ❤️ by ")
            withStyle(
                SpanStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Teo Vladusic")
            }
        },
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.spacing_xl)
            .clickable {
                AndroidIntentLauncher.openUrl(context = context, url = AppConstants.MAKER_URL)
            },
        textAlign = TextAlign.Center
    )
}

@Composable
private fun AppVersion() {
    Text(
        text = "App Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
        color = Color.White.copy(alpha = .5f),
        modifier = Modifier
            .padding(horizontal = Dimens.spacing_m)
            .padding(top = Dimens.spacing_xs)
    )
}

@Composable
private fun Info(executeAction: (SettingsAction) -> Unit) {
    SectionTitle(title = stringResource(R.string.info))
    InfoRow(
        label = stringResource(R.string.suggest_new_features),
        vector = Icons.Default.Lightbulb,
        onClick = { executeAction(SettingsAction.SuggestNewFeaturesClick) }
    )
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Dimens.spacing_m),
        color = Color.White.copy(alpha = .1f)
    )
    InfoRow(
        label = stringResource(R.string.rate_app),
        vector = Icons.Default.Star,
        onClick = { executeAction(SettingsAction.RateAppClick) }
    )
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Dimens.spacing_m),
        color = Color.White.copy(alpha = .1f)
    )
    InfoRow(
        label = stringResource(R.string.support),
        vector = Icons.Default.Email,
        onClick = { executeAction(SettingsAction.SupportClick) }
    )
}

@Composable
private fun InfoRow(label: String, vector: ImageVector, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Dimens.spacing_m, vertical = Dimens.spacing_s)
    ) {
        Icon(imageVector = vector, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(Dimens.spacing_xs))
        Text(text = label, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
private fun StripeProject(onClick: () -> Unit, projectName: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(color = Primary.copy(alpha = .1f), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = Dimens.spacing_m, vertical = Dimens.spacing_s)
    ) {
        Text(
            text = projectName ?: stringResource(R.string.no_project),
            color = Primary,
            fontSize = 14.sp,
        )
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Primary)
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        color = Color.White.copy(alpha = .6f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(horizontal = Dimens.spacing_m)
    )
}

@Composable
private fun UnlockPremiumFeaturesBanner(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Primary, shape = RoundedCornerShape(12.dp))
            .clip(shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = Dimens.spacing_m, vertical = Dimens.spacing_s),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color = Color.Black, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.img_crown),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(Modifier.width(Dimens.spacing_m))
        Text(
            text = stringResource(R.string.unlock_premium_features),
            color = Color.White,
            fontSize = 20.sp,
            maxLines = 1,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Content(
        state = SettingsUiState(),
        selectedProjectName = "Test project",
        executeAction = {}
    )
}
