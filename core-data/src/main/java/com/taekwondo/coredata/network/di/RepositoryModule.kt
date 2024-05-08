package com.taekwondo.coredata.network.di

import com.taekwondo.coredata.network.repository.AuthRepository
import com.taekwondo.coredata.network.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    @ViewModelScoped
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}