package com.teovladusic.widgetsforstripe.core.domain.use_case.add_project

import com.teovladusic.widgetsforstripe.core.data.model.StripeProject
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddProjectUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(projectData: StripeProject?) {
        projectData?.let {
            val projects = preferencesManager.stripeProjects.first()
            preferencesManager.addStripeProject(project = it)
            if (projects.isEmpty()) {
                preferencesManager.selectStripeProject(id = it.id)
            }
        }
        preferencesManager.setIsProjectAdded(isProjectAdded = true)
    }
}
