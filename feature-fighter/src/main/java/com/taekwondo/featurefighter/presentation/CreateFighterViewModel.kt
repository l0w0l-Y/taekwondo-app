package com.taekwondo.featurefighter.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.featurefighter.domain.FighterInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFighterViewModel @Inject constructor(
    private val interactor: FighterInteractor,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val uid = savedStateHandle.get<Long?>("uid")
    private val type = savedStateHandle.get<String?>("type")

    sealed class ScreenType
    object Update : ScreenType()
    object Read : ScreenType()
    object Create : ScreenType()

    sealed class State
    object NavigateMainState : State()
    object ErrorState : State()
    class UpdateFighterState(
        val uid: Long = 0L,
        val name: String,
        val age: String,
        val weight: String,
        val height: String,
        val weightCategory: String,
        val photo: String?,
    ) : State()

    val event = EventChannel<State>()

    /**
     * Получает модель бойца по uid.
     */
    init {
        viewModelScope.launch {
            if (uid != null) {
                interactor.getFighter(uid)
                    .doOnSuccess {
                        event.send(
                            UpdateFighterState(
                                uid = it.uid,
                                name = it.name,
                                age = it.age.toString(),
                                weight = it.weight.toString(),
                                height = it.height.toString(),
                                weightCategory = it.weightCategory,
                                photo = it.photo
                            )
                        )
                    }
            }
        }
    }

    /** Сохраняет бойца.
     * @param name имя бойца.
     * @param age возраст бойца.
     * @param weight вес бойца.
     * @param height рост бойца.
     * @param weightCategory весовая категория бойца.
     * @param photo фото бойца.
     * При пустых полях отправляет событие [ErrorState].
     * Если боец уже существует, обновляет его, отправляет событие [NavigateMainState], которое перенаправляет на главный экран.
     * При успешном создании отправляет событие [NavigateMainState], которое перенаправляет на главный экран.
     */
    fun onSaveClick(
        name: String,
        age: Float?,
        weight: Float?,
        height: Float?,
        weightCategory: String,
        photo: String?,
    ) {
        viewModelScope.launch {
            if (name.isEmpty() || age == null || weight == null || height == null || weightCategory.isEmpty() || photo == null) {
                event.send(ErrorState)
            } else {
                if (uid != null) {
                    interactor.updateFighter(uid, name, age, weight, height, weightCategory, photo)
                        .doOnSuccess {
                            event.send(NavigateMainState)
                        }
                } else {
                    interactor.createFighter(name, age, weight, height, weightCategory, photo)
                        .doOnSuccess {
                            event.send(NavigateMainState)
                        }
                }
            }
        }
    }

    /**
     * Удаляет бойца.
     * Перенаправляет на главный экран.
     */
    fun onDeleteFighter() {
        viewModelScope.launch {
            if (uid != null) {
                interactor.deleteFighter(uid)
                    .doOnSuccess {
                        event.send(NavigateMainState)
                    }
            }
        }
    }
}