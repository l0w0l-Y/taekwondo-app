package com.taekwondo.featureauth.di

import com.taekwondo.featureauth.domain.AuthInteractor
import com.taekwondo.featureauth.domain.AuthInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface AuthModule {
    @Binds
    @ViewModelScoped
    fun provideAuthInteractor(impl: AuthInteractorImpl): AuthInteractor
}