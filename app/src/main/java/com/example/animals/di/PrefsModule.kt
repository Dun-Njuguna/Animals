package com.example.animals.di

import android.app.Application
import com.example.animals.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

const val CONTEXT_APP = "Application context"
const val CONTEXT_ACTIVITY = "Activity context"

@Module
open class PrefsModule {

    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_APP)
    open fun provideApplicationSharedPreferences(app: Application): SharedPreferencesHelper{
        return SharedPreferencesHelper(app)
    }

    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_ACTIVITY)
    fun provideActivitySharedPreferences(app: Application): SharedPreferencesHelper{
        return SharedPreferencesHelper(app)
    }
}

@Qualifier
annotation class TypeOfContext(val type: String)