package com.example.animals

import android.app.Application
import com.example.animals.di.PrefsModule
import com.example.animals.util.SharedPreferencesHelper

class PrefsModuleTests(private val mockPrefs: SharedPreferencesHelper): PrefsModule() {
    override fun provideApplicationSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }
}