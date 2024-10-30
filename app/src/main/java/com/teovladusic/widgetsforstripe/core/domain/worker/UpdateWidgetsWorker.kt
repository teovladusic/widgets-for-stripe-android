package com.teovladusic.widgetsforstripe.core.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import com.teovladusic.widgetsforstripe.core.data.repository.StripeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber

@HiltWorker
class UpdateWidgetsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val params: WorkerParameters,
    private val repository: StripeRepository,
    private val preferencesManager: PreferencesManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val id = preferencesManager.selectedStripeProject.first()
            val projects = preferencesManager.stripeProjects.first()
            repository.getSelectedStripeProjectWithCharges(id = id, projects = projects).also {
                it?.let { repository.updateWidgetsAndSaveLocal(it) }
            }
            repository.getProjectWithMrr(id = id, projects = projects).also {
                it?.let { repository.updateMRRWidgetAndSaveLocal(it) }
            }
            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.retry()
        }
    }
}
