package com.teovladusic.widgetsforstripe.feature.add_project

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.widgetsforstripe.core.data.model.StripeProject
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import com.teovladusic.widgetsforstripe.core.domain.use_case.add_project.AddProjectUseCase
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import com.teovladusic.widgetsforstripe.core.util.constants.AndroidIntentLauncher
import com.teovladusic.widgetsforstripe.core.util.constants.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProjectViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context,
    private val addProjectUseCase: AddProjectUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddProjectUiState())
    val state: StateFlow<AddProjectUiState> = _state

    init {
        listenIsProjectAdded()
    }

    fun executeAction(action: AddProjectAction) {
        when (action) {
            is AddProjectAction.ApiKeyChanged -> {
                _state.update { it.copy(apiKey = action.apiKey) }
                enableSaveButtonIfNeeded()
            }

            AddProjectAction.BackClick -> viewModelScope.launch { mainNavigator.navigateBack() }

            AddProjectAction.GenerateKeyClick ->
                AndroidIntentLauncher.openUrl(context, AppConstants.STRIPE_GENERATE_KEY_URL)

            is AddProjectAction.ProjectNameChanged -> {
                _state.update { it.copy(projectName = action.name) }
                enableSaveButtonIfNeeded()
            }

            AddProjectAction.SaveProjectClick -> addProject(skip = false)
            AddProjectAction.SkipClick -> addProject(skip = true)
        }
    }

    private fun addProject(skip: Boolean) {
        viewModelScope.launch {
            val data = if (skip) null
            else StripeProject(apiKey = _state.value.apiKey, projectName = _state.value.projectName)

            addProjectUseCase(projectData = data)
            mainNavigator.navigateToStartDestination()
        }
    }

    private fun listenIsProjectAdded() {
        viewModelScope.launch {
            preferencesManager.isProjectAdded.collectLatest { isProjectAdded ->
                _state.update { it.copy(isProjectAdded = isProjectAdded) }
            }
        }
    }

    private fun enableSaveButtonIfNeeded() {
        _state.update {
            val enable = it.apiKey.startsWith("rk_") && it.projectName.isNotBlank()
            it.copy(isSaveButtonEnabled = enable)
        }
    }
}

data class AddProjectUiState(
    val apiKey: String = "",
    val projectName: String = "",
    val isProjectAdded: Boolean = false,
    val isSaveButtonEnabled: Boolean = false
)

sealed interface AddProjectAction {
    data object SkipClick : AddProjectAction
    data object BackClick : AddProjectAction
    data object SaveProjectClick : AddProjectAction
    data class ApiKeyChanged(val apiKey: String) : AddProjectAction
    data class ProjectNameChanged(val name: String) : AddProjectAction
    data object GenerateKeyClick : AddProjectAction
}
