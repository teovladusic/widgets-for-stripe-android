package com.teovladusic.widgetsforstripe.feature.finish_on_boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.widgetsforstripe.core.domain.use_case.on_boarding.FinishOnBoardingUseCase
import com.teovladusic.widgetsforstripe.core.navigation.navigator.MainNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishOnBoardingViewModel @Inject constructor(
    private val mainNavigator: MainNavigator,
    private val finishOnBoardingUseCase: FinishOnBoardingUseCase
) : ViewModel() {

    fun onFinishClick() {
        viewModelScope.launch {
            finishOnBoardingUseCase()
            mainNavigator.navigateToStartDestination()
        }
    }
}
