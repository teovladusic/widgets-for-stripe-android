package com.teovladusic.widgetsforstripe.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val navigator: MainNavigator,
) : ViewModel() {

    fun onContinueClick() {
        viewModelScope.launch {
        navigator.navigateToFinishOnBoardingScreen()
        }
    }
}
