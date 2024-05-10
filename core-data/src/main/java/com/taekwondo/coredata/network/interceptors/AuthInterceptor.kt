package com.taekwondo.coredata.network.interceptors

import com.taekwondo.coredata.network.database.DataStoreProvider
import com.taekwondo.coredata.network.database.UID_KEY
import com.taekwondo.coredata.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val prefixToken: String = "Bearer"

class AuthInterceptor @Inject constructor(
    dataStore: DataStoreProvider,
    @ApplicationScope mainScope: CoroutineScope
) : Interceptor {

    private var token = 0

    init {
        dataStore.get(UID_KEY)
            .distinctUntilChanged()
            .onEach { token = it ?: 0 }
            .launchIn(mainScope)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "$prefixToken $token")
            .build()
        return chain.proceed(request)
    }
}