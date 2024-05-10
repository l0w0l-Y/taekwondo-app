package com.taekwondo.featureauth.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnError
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.featureauth.domain.AuthInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor,
) : ViewModel() {
    sealed class State
    object NavigateMainState : State()
    object ErrorState : State()

    val event = EventChannel<State>()

    fun auth(email: String, password: String) {
        viewModelScope.launch {
            interactor
                .auth(email, password)
                .doOnSuccess {
                    event.send(NavigateMainState)
                }
                .doOnError {
                    event.send(ErrorState)
                }
        }
    }
}