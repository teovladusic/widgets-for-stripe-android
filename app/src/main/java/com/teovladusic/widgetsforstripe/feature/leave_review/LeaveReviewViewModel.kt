package com.teovladusic.widgetsforstripe.feature.leave_review

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.widgetsforstripe.core.data.preferences.PreferencesManager
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import com.teovladusic.widgetsforstripe.core.util.review.NativeReviewManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveReviewViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val nativeReviewManager: NativeReviewManager,
    private val mainNavigator: MainNavigator
) : ViewModel() {

    fun onContinueClick(activity: Activity) {
        viewModelScope.launch {
            nativeReviewManager.requestReview(activity)
            preferencesManager.setIsReviewScreenShown(isReviewScreenShown = true)
            mainNavigator.navigateToStartDestination()
        }
    }
}
