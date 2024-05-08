package com.taekwondo.featureauth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.corecommon.ext.debug
import com.taekwondo.coredata.network.doOnError
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.coredata.network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    sealed class State
    object NavigateMainState : State()

    val event = EventChannel<State>()
    fun onRegister(
        name: String,
        surname: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            repository.register(name, surname, email, password)
                .doOnSuccess {
                    event.send(NavigateMainState)
                }
                .doOnError {
                    debug(it)
                }
        }
    }
}