package com.teovladusic.widgetsforstripe.feature.select_project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.widgetsforstripe.core.data.model.StripeProject
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectProjectViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val mainNavigator: MainNavigator
) : ViewModel() {

    val selectedProjectId: Flow<String?> = preferencesManager.selectedStripeProject
    val stripeProjects: Flow<List<StripeProject>> = preferencesManager.stripeProjects

    fun executeAction(action: SelectProjectAction) {
        when (action) {
            SelectProjectAction.AddNewProjectClick -> viewModelScope.launch {
                mainNavigator.navigateToAddProject()
            }

            SelectProjectAction.NavigateBackClick -> viewModelScope.launch { mainNavigator.navigateBack() }

            is SelectProjectAction.ProjectClick -> viewModelScope.launch {
                preferencesManager.selectStripeProject(action.project.id)
            }
        }
    }
}

sealed interface SelectProjectAction {
    data object NavigateBackClick : SelectProjectAction
    data object AddNewProjectClick : SelectProjectAction
    data class ProjectClick(val project: StripeProject) : SelectProjectAction
}
