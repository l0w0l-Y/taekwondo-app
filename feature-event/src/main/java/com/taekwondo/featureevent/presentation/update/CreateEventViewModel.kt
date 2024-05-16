package com.taekwondo.featureevent.presentation.update

import androidx.lifecycle.SavedStateHandle
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
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val uid = savedStateHandle.get<Long?>("uid")

    sealed class State
    object NavigateMainState : State()
    object ErrorState : State()

    val event = EventChannel<State>()

    sealed class ScreenType
    object Update : ScreenType()
    object Create : ScreenType()

    class UpdateEventState(
        val uid: Long,
        val name: String,
        val date: String,
        val place: String,
    ) : State()

    /**
     * Получает модель события по uid.
     */
    init {
        viewModelScope.launch {
            if (uid != null) {
                interactor.getEventModel(uid)
                    .doOnSuccess {
                        it?.let {
                            event.send(
                                UpdateEventState(
                                    uid = it.uid,
                                    name = it.name,
                                    date = it.date,
                                    place = it.place
                                )
                            )
                        }
                    }
            }
        }
    }

    /** Создает событие.
     * @param name имя события.
     * @param date дата события.
     * @param place место проведения события.
     * При пустых полях отправляет событие [ErrorState].
     * Если событие уже существует, обновляет его, отправляет событие [NavigateMainState], которое перенаправляет на главный экран.
     * При успешном создании отправляет событие [NavigateMainState], которое перенаправляет на главный экран.
     */
    fun createEvent(name: String, date: String, place: String) {
        viewModelScope.launch {
            if (name.isEmpty() || date.isEmpty() || place.isEmpty()) {
                event.send(ErrorState)
            } else {
                if (uid != null) {
                    interactor.updateEvent(uid, name, date, place)
                        .doOnSuccess {
                            event.send(NavigateMainState)
                        }
                } else {
                    interactor.createEvent(name, date, place)
                        .doOnSuccess {
                            event.send(NavigateMainState)
                        }
                }
            }
        }
    }
}