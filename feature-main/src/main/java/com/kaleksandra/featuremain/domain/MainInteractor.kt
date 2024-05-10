package com.kaleksandra.featuremain.domain

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.repository.MainRepository
import javax.inject.Inject

interface MainInteractor {
    suspend fun getAllFighters(): Effect<List<FighterEntity>>
    suspend fun logOut(): Effect<Unit>
}

class MainInteractorImpl @Inject constructor(
    private val mainRepository: MainRepository
) : MainInteractor {
    override suspend fun getAllFighters(): Effect<List<FighterEntity>> {
        return mainRepository.getAllFighters()
    }

    override suspend fun logOut(): Effect<Unit> {
        return mainRepository.logOut()
    }
}