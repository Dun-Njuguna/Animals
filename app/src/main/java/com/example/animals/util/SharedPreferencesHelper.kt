package com.example.animals.util

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {

    private val PREF_APi_KEY = "Api Key"
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    fun saveApiKey(key:String){
        prefs.edit().putString(PREF_APi_KEY, key).apply()
    }

    fun getApiKey():String? {
        return prefs.getString(PREF_APi_KEY, null)
    }

}