package com.taekwondo.featurefighter.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taekwondo.corecommon.ext.EventChannel
import com.taekwondo.coredata.network.doOnSuccess
import com.taekwondo.coredata.network.enums.Gender
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
        val club: String,
        val trainer: String,
        val gender: Gender,
        val photo: String?,
        val totalFights: Int,
        val wins: Int,
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
                                club = it.club,
                                trainer = it.trainer,
                                gender = it.gender,
                                photo = it.photo,
                                totalFights = it.totalFights,
                                wins = it.wins
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
        gender: Gender,
        club: String,
        trainer: String,
    ) {
        viewModelScope.launch {
            if (name.isEmpty() || age == null || weight == null || height == null || weightCategory.isEmpty() || club.isEmpty() || trainer.isEmpty()) {
                event.send(ErrorState)
            } else {
                if (uid != null) {
                    interactor.updateFighter(
                        uid,
                        name,
                        age,
                        weight,
                        height,
                        weightCategory,
                        gender,
                        club,
                        trainer,
                        photo
                    )
                        .doOnSuccess {
                            event.send(NavigateMainState)
                        }
                } else {
                    interactor.createFighter(
                        name,
                        age,
                        weight,
                        height,
                        weightCategory,
                        gender,
                        club,
                        trainer,
                        photo
                    )
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

    fun onUploadClick(uri: Uri) {
        viewModelScope.launch {
            interactor.readExcelFile(uri).doOnSuccess {
                event.send(NavigateMainState)
            }
        }
    }
}