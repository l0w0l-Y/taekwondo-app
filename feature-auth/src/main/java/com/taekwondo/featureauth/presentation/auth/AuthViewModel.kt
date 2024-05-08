package com.taekwondo.featureauth.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.coredata.network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    sealed class State
    object NavigateMainState : State()

    val event = EventChannel<State>()

    fun auth(email: String, password: String) {
        viewModelScope.launch {
            repository
                .auth(email, password)
                .doOnSuccess {
                    event.send(NavigateMainState)
                }
        }
    }
}