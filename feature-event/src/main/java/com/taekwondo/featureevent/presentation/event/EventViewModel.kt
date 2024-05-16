package com.taekwondo.featureevent.presentation.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.featureevent.domain.EventInteractor
import com.taekwondo.featureevent.presentation.model.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val interactor: EventInteractor,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val uid = checkNotNull(savedStateHandle.get<Long>("uid"))

    private val _eventModel = MutableStateFlow<EventModel?>(null)
    val eventModel: StateFlow<EventModel?> = _eventModel

    val event = EventChannel<State>()

    sealed class State
    class NavigateUpdateParticipantsState(val uid: Long) : State()
    class NavigateUpdateEventState(val uid: Long) : State()

    init {
        viewModelScope.launch {
            interactor.getEventModel(uid).doOnSuccess {
                _eventModel.emit(it)
            }
        }
    }

    fun updateParticipants() {
        viewModelScope.launch {
            event.send(NavigateUpdateParticipantsState(uid))
        }
    }

    fun onUpdateEvent(){
        viewModelScope.launch {
            event.send(NavigateUpdateEventState(uid))
        }
    }
}