package com.taekwondo.featuremain.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.featuremain.domain.MainInteractor
import com.taekwondo.featuremain.presentation.model.EventModel
import com.taekwondo.featuremain.presentation.model.FighterModel
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor,
) : ViewModel() {

    private val _fighters = MutableStateFlow<List<FighterModel>>(emptyList())
    val fighters: StateFlow<List<FighterModel>> = _fighters

    private val _events = MutableStateFlow<List<EventModel>>(emptyList())
    val events: StateFlow<List<EventModel>> = _events

    sealed class State
    object NavigateAuthState : State()

    val event = EventChannel<State>()

    init {
        viewModelScope.launch {
            mainInteractor.getAllFighters()
                .doOnSuccess {
                    _fighters.emit(it.map {
                        FighterModel(
                            uid = it.uid,
                            name = it.name,
                            age = it.age,
                            weight = it.weight,
                            height = it.height,
                            weightCategory = it.weightCategory,
                            photo = it.photo
                        )
                    })
                }
            mainInteractor.getAllEvents().doOnSuccess {
                _events.emit(
                    it.map {
                        EventModel(
                            uid = it.uid,
                            name = it.name,
                            date = it.date,
                            place = it.place,
                        )
                    }
                )
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            mainInteractor.logOut().doOnSuccess {
                event.send(NavigateAuthState)
            }
        }
    }
}