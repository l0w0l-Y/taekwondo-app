package com.taekwondo.taekwondoapp

import com.taekwondo.coredata.network.database.DataStoreProvider
import com.taekwondo.coredata.network.database.UID_KEY
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val dataStore: DataStoreProvider
) {
    fun getToken(): Flow<Boolean> {
        return dataStore.contains(UID_KEY)
    }
}