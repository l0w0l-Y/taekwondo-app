package com.kaleksandra.featuremain.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaleksandra.featuremain.domain.MainInteractor
import com.kaleksandra.featuremain.presentation.model.FighterModel
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _fighters = MutableStateFlow<List<FighterModel>>(emptyList())
    val fighters: StateFlow<List<FighterModel>> = _fighters

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
        }
    }

    fun logOut(){
        viewModelScope.launch {
            mainInteractor.logOut().doOnSuccess {
                event.send(NavigateAuthState)
            }
        }
    }
}