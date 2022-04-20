package com.example.diagnal.di

import android.content.Context
import com.example.diagnal.data.repository.MainRepositoryImpl
import com.example.diagnal.domain.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMainRepository(@ApplicationContext appContext: Context): MainRepository {
        return MainRepositoryImpl(appContext)
    }
}