package com.taekwondo.featureevent.presentation.fighters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.coredata.network.model.FighterModel
import com.taekwondo.featureevent.domain.EventInteractor
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

    private val uid = checkNotNull(savedStateHandle.get<Long?>("uid"))

    val mainJudges = MutableStateFlow(listOf<JudgeModel>())
    val judges = MutableStateFlow(listOf<JudgeModel>())
    val fighters = MutableStateFlow(listOf<FighterModel>())

    sealed class State
    class NavigateMainState(val uid: Long) : State()
    object ErrorMainJudgesState : State()
    object ErrorZeroJudgesState : State()
    object ErrorZeroFightersState : State()
    object ErrorCountFightersState : State()

    val event = EventChannel<State>()

    /**
     * Получает список судей и бойцов.
     * Получает модель события по uid.
     * При получении модели события, устанавливает флаги isPicked для судей и бойцов, которые уже участвуют в событии.
     */
    init {
        viewModelScope.launch {
            interactor.getAllFighters().doOnSuccess {
                fighters.emit(it)
            }
            interactor.getAllJudges().doOnSuccess {
                judges.emit(it)
                mainJudges.emit(it)
            }
            interactor.getEventModel(uid)
                .doOnSuccess {
                    it?.let {
                        it.mainJudge.let { user ->
                            mainJudges.value = mainJudges.value.map { judge ->
                                if (judge.uid == user?.uid) {
                                    judge.copy(isPicked = true)
                                } else {
                                    judge
                                }
                            }
                        }
                        it.judges.forEach { user ->
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

    /**
     * Обновляет флаг isPicked для судьи, если судья участвует или не участвует в событии.
     */
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

    /**
     * Обновляет флаг isPicked для главного судьи, если судья участвует или не участвует в событии.
     */
    fun updateMainJudge(judge: JudgeModel) {
        val updatedJudges = mainJudges.value.map { currentJudge ->
            if (currentJudge.uid == judge.uid) {
                currentJudge.copy(isPicked = !currentJudge.isPicked)
            } else {
                currentJudge
            }
        }
        mainJudges.value = updatedJudges
    }

    /**
     * Обновляет флаг isPicked для бойца, если боец участвует или не участвует в событии.
     */
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

    /**
     * Обновляет список участников события.
     * Перенаправляет на главный экран.
     */
    fun updateEventFighter(
        judges: List<JudgeModel>,
        fighters: List<FighterModel>,
        mainJudges: List<JudgeModel>,
    ) {
        viewModelScope.launch {
            if (fighters.count { it.isPicked } == 0) {
                event.send(ErrorZeroFightersState)
                return@launch
            }
            if (fighters.count { it.isPicked } != 2 && fighters.count { it.isPicked } != 4 &&
                fighters.count { it.isPicked } != 8 && fighters.count { it.isPicked } != 16 &&
                fighters.count { it.isPicked } != 32) {
                event.send(ErrorCountFightersState)
                return@launch
            }
            if (judges.count { it.isPicked } == 0) {
                event.send(ErrorZeroJudgesState)
                return@launch
            }
            if (mainJudges.count { it.isPicked } != 1) {
                event.send(ErrorMainJudgesState)
                return@launch
            }
            interactor.insertEventParticipants(
                uid,
                judges.filter { it.isPicked }.map { it.uid },
                fighters.filter { it.isPicked }.map { it.uid },
                mainJudges.first { it.isPicked }.uid,
            )
                .doOnSuccess {
                    event.send(NavigateMainState(uid))
                }
        }
    }
}