package com.teovladusic.widgetsforstripe.feature.add_project

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.components.WFSPrimaryButton
import com.teovladusic.widgetsforstripe.core.design_system.components.WFSTextInput
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary

@Destination
@Composable
fun AddProjectScreen(viewModel: AddProjectViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Content(state = state, executeAction = viewModel::executeAction)
}

@Composable
private fun Content(state: AddProjectUiState, executeAction: (AddProjectAction) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        topBar = { Toolbar(state = state, executeAction = executeAction) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = Dimens.spacing_m)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(state = rememberScrollState())
            ) {
                Spacer(Modifier.height(Dimens.spacing_m))
                Text(
                    text = stringResource(R.string.add_stripe_project),
                    color = Primary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = NunitoFontFamily
                )
                Spacer(Modifier.height(Dimens.spacing_m))
                GenerateKey { executeAction(AddProjectAction.GenerateKeyClick) }
                Spacer(Modifier.height(Dimens.spacing_xl))
                DoNotChangePermission()
                Spacer(Modifier.height(Dimens.spacing_xl))
                ApiKeyInput(state = state, executeAction = executeAction)
                Spacer(Modifier.height(Dimens.spacing_xl))
                ProjectNameInput(state = state, executeAction = executeAction)
            }
            Column {
                Spacer(Modifier.height(Dimens.spacing_xxl))
                WFSPrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.save_project),
                    onClick = { executeAction(AddProjectAction.SaveProjectClick) },
                    enabled = state.isSaveButtonEnabled,
                    shape = RoundedCornerShape(64.dp)
                )
                Spacer(Modifier.height(Dimens.spacing_m))
            }
        }
    }
}

@Composable
private fun ProjectNameInput(state: AddProjectUiState, executeAction: (AddProjectAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.project_name),
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(Dimens.spacing_m))
        WFSTextInput(
            modifier = Modifier.fillMaxWidth(),
            text = state.projectName,
            placeholder = stringResource(R.string.project_name_placeholder),
            onValueChange = { executeAction(AddProjectAction.ProjectNameChanged(it)) }
        )
    }
}

@Composable
private fun ApiKeyInput(state: AddProjectUiState, executeAction: (AddProjectAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BulletPoint(number = 3, text = stringResource(R.string.copy_paste_api_key_here))
        Spacer(Modifier.height(Dimens.spacing_m))
        WFSTextInput(
            modifier = Modifier.fillMaxWidth(),
            text = state.apiKey,
            placeholder = stringResource(R.string.api_key_placeholder),
            onValueChange = { executeAction(AddProjectAction.ApiKeyChanged(it)) }
        )
        Spacer(Modifier.height(Dimens.spacing_s))
        Text(
            text = stringResource(R.string.api_key_explanation),
            color = Color.White.copy(alpha = .4f),
            fontSize = 13.sp
        )
    }
}

@Composable
private fun DoNotChangePermission() {
    BulletPoint(number = 2, text = stringResource(R.string.do_not_change_permission))
}

@Composable
private fun GenerateKey(onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BulletPoint(number = 1, text = stringResource(R.string.generate_api_key))
        Spacer(Modifier.height(Dimens.spacing_m))
        WFSPrimaryButton(
            text = stringResource(R.string.generate_key),
            modifier = Modifier
                .fillMaxWidth(fraction = .6f)
                .align(Alignment.CenterHorizontally),
            onClick = onClick,
            shape = RoundedCornerShape(64.dp)
        )
    }
}

@Composable
private fun BulletPoint(number: Int, text: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = number.toString(),
            color = Primary,
            fontSize = 20.sp,
            modifier = Modifier
                .size(28.dp)
                .background(color = Primary.copy(alpha = .3f), shape = CircleShape),
            textAlign = TextAlign.Center,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(Dimens.spacing_m))
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(state: AddProjectUiState, executeAction: (AddProjectAction) -> Unit) {
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
        navigationIcon = {
            if (state.isProjectAdded) {
                IconButton(onClick = { executeAction(AddProjectAction.BackClick) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        tint = Primary
                    )
                }
            }
        },
        actions = {
            if (!state.isProjectAdded) {
                TextButton(onClick = { executeAction(AddProjectAction.SkipClick) }) {
                    Text(text = stringResource(R.string.skip), color = Primary)
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    Content(state = AddProjectUiState(), executeAction = {})
}
