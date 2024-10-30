package com.teovladusic.widgetsforstripe.feature.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val navigator: MainNavigator
) : ViewModel() {

    fun onStartClick() {
        viewModelScope.launch {
            navigator.navigateToOnboardingScreen()
        }
    }
}
