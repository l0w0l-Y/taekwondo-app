package com.taekwondo.featureevent.presentation.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.featureevent.domain.EventInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val interactor: EventInteractor,
) : ViewModel() {

    sealed class State
    object NavigateMainState : State()
    object ErrorState : State()

    val event = EventChannel<State>()

    fun createEvent(name: String, date: String, place: String) {
        viewModelScope.launch {
            if (name.isEmpty() || date.isEmpty() || place.isEmpty()) {
                event.send(ErrorState)
            } else {
                interactor.createEvent(name, date, place).doOnSuccess {
                    event.send(NavigateMainState)
                }
            }
        }
    }
}