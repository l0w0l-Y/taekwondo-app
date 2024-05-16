package com.taekwondo.featureevent.presentation.fighters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.featureevent.domain.EventInteractor
import com.taekwondo.featureevent.presentation.model.FighterModel
import com.taekwondo.featureevent.presentation.model.JudgeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateEventFighterViewModel @Inject constructor(
    private val interactor: EventInteractor,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val uid = checkNotNull(savedStateHandle.get<Int?>("uid"))

    val judges = MutableStateFlow(listOf<JudgeModel>())
    val fighters = MutableStateFlow(listOf<FighterModel>())

    sealed class State
    class NavigateEventState(val uid: Int) : State()

    val event = EventChannel<State>()

    init {
        viewModelScope.launch {
            interactor.getAllFighters().doOnSuccess {
                fighters.emit(it)
            }
            interactor.getAllUsers().doOnSuccess {
                judges.emit(it)
            }
            interactor.getEventModel(uid)
                .doOnSuccess {
                    it?.let {
                        it.users.forEach { user ->
                            judges.value = judges.value.map { judge ->
                                if (judge.uid == user.uid) {
                                    judge.copy(isPicked = true)
                                } else {
                                    judge
                                }
                            }
                        }
                        it.fighters.forEach { user ->
                            fighters.value = fighters.value.map { judge ->
                                if (judge.uid == user.uid) {
                                    judge.copy(isPicked = true)
                                } else {
                                    judge
                                }
                            }
                        }
                    }
                }
        }
    }

    fun updateJudges(judge: JudgeModel) {
        val updatedJudges = judges.value.map { currentJudge ->
            if (currentJudge.uid == judge.uid) {
                currentJudge.copy(isPicked = !currentJudge.isPicked)
            } else {
                currentJudge
            }
        }
        judges.value = updatedJudges
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

    fun updateEventFighter(users: List<JudgeModel>, fighters: List<FighterModel>) {
        viewModelScope.launch {
            interactor.insertEventParticipants(
                uid,
                users.map { it.uid },
                fighters.map { it.uid })
                .doOnSuccess {
                    event.send(NavigateEventState(uid))
                }
        }
    }
}