package com.taekwondo.featureauth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.corecommon.ext.ResourcesProvider
import com.taekwondo.coredata.network.doOnError
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.featureauth.R
import com.taekwondo.featureauth.domain.AuthInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val interactor: AuthInteractor,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    sealed class State
    object NavigateMainState : State()
    class ErrorState(val message: String) : State()

    val event = EventChannel<State>()

    /**
     * Регистрирует пользователя.
     * @param name имя пользователя.
     * @param username логин пользователя.
     * @param email почта пользователя.
     * @param password пароль пользователя.
     * @param phone телефон пользователя.
     * @param photo фото пользователя.
     * При успешной регистрации отправляет событие [NavigateMainState], которое перенаправляет на главный экран.
     * При ошибке отправляет событие [ErrorState], которое выводит ошибку.
     */
    fun onRegister(
        name: String,
        username: String,
        email: String,
        password: String,
        phone: String,
        photo: String?
    ) {
        viewModelScope.launch {
            if (name == "" || username == "" || email == "" || password == "" || phone == "" || photo == null) {
                event.send(ErrorState(message = resourcesProvider.getString(R.string.error_field_required)))
            } else {
                interactor.register(name, username, email, password, phone, photo)
                    .doOnSuccess {
                        event.send(NavigateMainState)
                    }
                    .doOnError {
                        event.send(ErrorState(message = resourcesProvider.getString(R.string.error_field_required)))
                    }
            }
        }
    }
}