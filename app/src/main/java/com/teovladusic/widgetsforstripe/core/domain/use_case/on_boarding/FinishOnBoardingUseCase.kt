package com.teovladusic.widgetsforstripe.core.domain.use_case.on_boarding

import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import javax.inject.Inject

class FinishOnBoardingUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke() {
        preferencesManager.setIsOnBoarded(isOnBoarded = true)
    }
}
