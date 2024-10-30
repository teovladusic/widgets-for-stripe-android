package com.teovladusic.widgetsforstripe.feature.select_project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.data.model.StripeProject
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary

@Destination
@Composable
fun SelectProjectScreen(viewModel: SelectProjectViewModel = hiltViewModel()) {
    val projects by viewModel.stripeProjects.collectAsStateWithLifecycle(emptyList())
    val selectedProject by viewModel.selectedProjectId.collectAsStateWithLifecycle(null)

    Content(
        projects = projects,
        selectedProjectId = selectedProject,
        executeAction = viewModel::executeAction
    )
}

@Composable
private fun Content(
    projects: List<StripeProject>,
    selectedProjectId: String?,
    executeAction: (SelectProjectAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        topBar = { Toolbar(executeAction = executeAction) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = Dimens.spacing_m)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = .1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
            ) {
                items(projects) { project ->
                    Project(
                        project = project,
                        isSelected = project.id == selectedProjectId,
                        onClick = { executeAction(SelectProjectAction.ProjectClick(project)) }
                    )
                    if (project.id != projects.last().id) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = Dimens.spacing_m),
                            color = Color.White.copy(alpha = .2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Project(project: StripeProject, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Dimens.spacing_m, vertical = Dimens.spacing_s),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = project.projectName,
            color = Color.White,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
        )
        if (isSelected) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Primary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(executeAction: (SelectProjectAction) -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.stripe_projects),
                color = Primary,
                fontSize = 17.sp,
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Medium
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black),
        navigationIcon = {
            IconButton(onClick = { executeAction(SelectProjectAction.NavigateBackClick) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Primary
                )
            }
        },
        actions = {
            IconButton(onClick = { executeAction(SelectProjectAction.AddNewProjectClick) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Primary
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    Content(
        projects = listOf(
            StripeProject(id = "nisl", apiKey = "nunc", projectName = "Rhea Howard"),
            StripeProject(id = "nisl2", apiKey = "nunc", projectName = "Rhea Howard"),
            StripeProject(id = "nisl3", apiKey = "nunc", projectName = "Rhea Howard")
        ),
        selectedProjectId = "nisl",
        executeAction = {}
    )
}