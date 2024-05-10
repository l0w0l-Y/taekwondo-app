package com.taekwondo.featureevent.di

import com.taekwondo.featureevent.domain.EventInteractor
import com.taekwondo.featureevent.domain.EventInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface EventModule {
    @Binds
    @ViewModelScoped
    fun provideEventInteractor(impl: EventInteractorImpl): EventInteractor
}