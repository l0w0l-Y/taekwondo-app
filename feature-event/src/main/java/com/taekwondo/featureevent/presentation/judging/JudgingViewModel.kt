package com.taekwondo.featureevent.presentation.judging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.corecommon.ext.debug
import com.taekwondo.coredata.network.database.DataStoreProvider
import com.taekwondo.coredata.network.database.UID_KEY
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.coredata.network.model.FightModel
import com.taekwondo.coredata.network.model.FighterModel
import com.taekwondo.featureevent.domain.EventInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JudgingViewModel @Inject constructor(
    private val interactor: EventInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val dataStoreProvider: DataStoreProvider,
) : ViewModel() {
    private val eventId = checkNotNull(savedStateHandle.get<Long>("eventId"))
    private val tournamentId = checkNotNull(savedStateHandle.get<Long>("fightId"))
    private var judgeId = 0L
    val round = MutableStateFlow(1)

    sealed class State
    object ErrorState : State()

    val event = EventChannel<State>()
    val selectedFighters = MutableStateFlow(listOf<FighterModel>())

    init {
        dataStoreProvider.get(UID_KEY)
            .distinctUntilChanged()
            .onEach { judgeId = it ?: 0L }
            .launchIn(viewModelScope)
        viewModelScope.launch {
            interactor.getTournament(tournamentId).doOnSuccess {
                if (it?.fighter1 != null && it.fighter2 != null) {
                    selectedFighters.emit(
                        listOf(
                            it.fighter1!!.copy(isPicked = true),
                            it.fighter2!!.copy(isPicked = true),
                        )
                    )
                }
            }
        }
    }

    fun savePoints(
        list: Pair<Float, Float>,
    ) {
        viewModelScope.launch {
            interactor.savePoints(
                FightModel(
                    tournamentId,
                    eventId,
                    selectedFighters.value.getOrNull(0)?.uid ?: return@launch,
                    selectedFighters.value.getOrNull(1)?.uid
                        ?: selectedFighters.value.getOrNull(0)!!.uid,
                    judgeId,
                    list.first.toInt(),
                    list.second.toInt(),
                )
            ).doOnSuccess {
                round.value++
            }
        }
    }
}