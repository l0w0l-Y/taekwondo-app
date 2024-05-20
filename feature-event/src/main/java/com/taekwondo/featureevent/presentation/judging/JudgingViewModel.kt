package com.taekwondo.featureevent.presentation.judging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.database.DataStoreProvider
import com.taekwondo.coredata.network.database.UID_KEY
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.coredata.network.model.FightModel
import com.taekwondo.featureevent.domain.EventInteractor
import com.taekwondo.featureevent.presentation.model.FighterModel
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
    private val dataStoreProvider: DataStoreProvider
) : ViewModel() {
    private val eventId = checkNotNull(savedStateHandle.get<Long>("eventId"))
    private var judgeId = 0L
    val fighters = MutableStateFlow(listOf<FighterModel>())
    val round = MutableStateFlow(1)

    sealed class State
    object JudgingState : State()
    object ErrorState : State()

    val event = EventChannel<State>()
    val selectedFighters = MutableStateFlow(listOf<FighterModel>())

    init {
        dataStoreProvider.get(UID_KEY)
            .distinctUntilChanged()
            .onEach { judgeId = it ?: 0L }
            .launchIn(viewModelScope)
        viewModelScope.launch {
            interactor.getFightersEvent(eventId).doOnSuccess {
                fighters.emit(it)
            }
        }
    }

    fun savePoints(
        list: List<Float>,
    ) {
        viewModelScope.launch {
            interactor.savePoints(
                list.mapIndexed { index, points ->
                    FightModel(
                        eventId,
                        selectedFighters.value.getOrNull(index)?.uid ?: return@launch,
                        judgeId,
                        points.toInt(),
                        round.value
                    )
                }
            ).doOnSuccess {
                round.value++
            }
        }
    }

    fun updateFighters(fighter: FighterModel) {
        val updatedFighter = fighters.value.map { currentFighter ->
            if (currentFighter.uid == fighter.uid) {
                currentFighter.copy(isPicked = !currentFighter.isPicked)
            } else {
                currentFighter
            }
        }
        fighters.value = updatedFighter
    }

    fun onCheckFighters() {
        viewModelScope.launch {
            if (fighters.value.count { it.isPicked } > 2 || fighters.value.count { it.isPicked } == 0) {
                event.send(ErrorState)
            } else {
                selectedFighters.value = fighters.value.filter { it.isPicked }
                event.send(JudgingState)
            }
        }
    }
}