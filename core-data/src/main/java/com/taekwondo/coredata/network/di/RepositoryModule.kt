package com.taekwondo.coredata.network.di

import com.taekwondo.coredata.network.repository.AuthRepository
import com.taekwondo.coredata.network.repository.AuthRepositoryImpl
import com.taekwondo.coredata.network.repository.FighterRepository
import com.taekwondo.coredata.network.repository.FighterRepositoryImpl
import com.taekwondo.coredata.network.repository.MainRepository
import com.taekwondo.coredata.network.repository.MainRepositoryImpl
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

    @Binds
    @ViewModelScoped
    fun provideFighterRepository(impl: FighterRepositoryImpl): FighterRepository

    @Binds
    @ViewModelScoped
    fun provideMainRepository(impl: MainRepositoryImpl): MainRepository
}