package com.taekwondo.featurefighter.di

import com.taekwondo.featurefighter.domain.FighterInteractor
import com.taekwondo.featurefighter.domain.FighterInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface FighterModule {
    @Binds
    @ViewModelScoped
    fun provideFighterInteractor(impl: FighterInteractorImpl): FighterInteractor
}