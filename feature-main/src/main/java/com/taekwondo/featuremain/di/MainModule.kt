package com.taekwondo.featuremain.di

import com.taekwondo.featuremain.domain.MainInteractor
import com.taekwondo.featuremain.domain.MainInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface MainModule {
    @Binds
    @ViewModelScoped
    fun provideMainInteractor(impl: MainInteractorImpl): MainInteractor
}