package com.qanti.myapplication.di

import android.content.Context
import com.qanti.myapplication.data.localDataSource.TokenDataSource
import com.qanti.myapplication.data.localDataSource.TokenDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideTokenDataSource(@ApplicationContext context: Context): TokenDataSource {
        return TokenDataSourceImpl(context)
    }
}