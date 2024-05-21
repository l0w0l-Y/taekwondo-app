package com.taekwondo.featureevent.presentation.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.database.DataStoreProvider
import com.taekwondo.coredata.network.database.UID_KEY
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.coredata.network.model.ResultFighterModel
import com.taekwondo.featureevent.domain.EventInteractor
import com.taekwondo.featureevent.presentation.model.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val interactor: EventInteractor,
    dataStoreProvider: DataStoreProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val isJudgingAvailable = MutableStateFlow(false)

    private val _eventModel = MutableStateFlow<EventModel?>(null)
    val eventModel: StateFlow<EventModel?> = _eventModel

    private val _resultModel = MutableStateFlow<List<ResultFighterModel>>(emptyList())
    val resultModel: StateFlow<List<ResultFighterModel>> = _resultModel

    private val uid = checkNotNull(savedStateHandle.get<Long>("uid"))
    private var judgeId = -1L

    val event = EventChannel<State>()

    sealed class State
    class NavigateUpdateParticipantsState(val uid: Long) : State()
    class NavigateUpdateEventState(val uid: Long) : State()
    class NavigateMainState(val uid: Long) : State()

    /**
     * Получает модель события по uid.
     */
    init {
        dataStoreProvider.get(UID_KEY)
            .distinctUntilChanged()
            .onEach {
                judgeId = it ?: 0L
                isJudgingAvailable.emit(
                    _eventModel.value?.judges?.any { judge -> judge.uid == judgeId } ?: false
                            || _eventModel.value?.mainJudge?.uid == judgeId
                )
            }
            .launchIn(viewModelScope)
        viewModelScope.launch {
            interactor.getEventModel(uid)
                .doOnSuccess {
                    _eventModel.emit(it)
                    isJudgingAvailable.emit(
                        it?.judges?.any { judge -> judge.uid == judgeId } ?: false
                                || it?.mainJudge?.uid == judgeId
                    )
                }
            interactor.getResults(uid).doOnSuccess {
                _resultModel.value = it
            }
        }
    }

    /**
     * Перенаправляет на экран обновления участников.
     */
    fun onUpdateParticipants() {
        viewModelScope.launch {
            event.send(NavigateUpdateParticipantsState(uid))
        }
    }

    /**
     * Перенаправляет на экран обновления события.
     */
    fun onUpdateEvent() {
        viewModelScope.launch {
            event.send(NavigateUpdateEventState(uid))
        }
    }

    fun onCompleteEvent() {
        viewModelScope.launch {
            interactor.archiveEvent(uid)
                .doOnSuccess {
                    event.send(NavigateMainState(uid))
                }
        }
    }

    fun onDeleteEvent() {
        viewModelScope.launch {
            interactor.deleteEvent(uid)
                .doOnSuccess {
                    event.send(NavigateMainState(uid))
                }
        }
    }
}