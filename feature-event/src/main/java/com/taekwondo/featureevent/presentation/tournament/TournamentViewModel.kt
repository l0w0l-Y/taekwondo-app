package com.taekwondo.featureevent.presentation.tournament

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.corecommon.ext.debug
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.coredata.network.model.TournamentModel
import com.taekwondo.featureevent.domain.EventInteractor
import com.taekwondo.featureevent.presentation.model.EventStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TournamentViewModel @Inject constructor(
    private val eventInteractor: EventInteractor,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _tournament = MutableStateFlow<List<TournamentModel>>(emptyList())
    val tournament: StateFlow<List<TournamentModel>> = _tournament

    val isJudgingAvailable = MutableStateFlow(false)

    val round = MutableStateFlow<Int>(0)

    private val eventId = checkNotNull(savedStateHandle.get<Long>("eventId"))

    sealed class State
    class JudgingState(val eventId: Long, val fightId: Long) : State()

    val event = EventChannel<State>()

    init {
        viewModelScope.launch {
            eventInteractor.getEventModel(eventId).doOnSuccess {
                isJudgingAvailable.emit(it?.status == EventStatus.IN_PROGRESS)
            }
        }
        getTournament()
    }

    fun onJudge(matchId: Long) {
        viewModelScope.launch {
            event.send(JudgingState(eventId, matchId))
        }
    }

    fun onComplete(matchId: Long) {
        viewModelScope.launch {
            eventInteractor.getWinner(eventId, matchId).doOnSuccess {
                getTournament()
            }
        }
    }

    private fun getTournament() {
        viewModelScope.launch {
            eventInteractor.getTournamentEvent(eventId).doOnSuccess {
                _tournament.emit(it)
                val _round = it.maxBy { it.round }.round
                round.emit(_round)
            }
        }
    }
}