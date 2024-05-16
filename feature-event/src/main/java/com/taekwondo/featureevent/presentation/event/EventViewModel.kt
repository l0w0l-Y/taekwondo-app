package com.taekwondo.featureevent.presentation.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val uid = checkNotNull(savedStateHandle.get<Int>("uid"))

    private val _event = MutableStateFlow<EventModel?>(null)
    val event: StateFlow<EventModel?> = _event

    init {
        viewModelScope.launch {
            interactor.getEventModel(uid).doOnSuccess {
                _event.emit(it)
            }
        }
    }
}