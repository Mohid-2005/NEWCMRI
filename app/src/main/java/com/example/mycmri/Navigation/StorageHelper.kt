package com.example.mycmri.helpers  // Use your actual package name

import android.content.Context
import android.content.SharedPreferences

object StorageHelper {
    private const val PREF_NAME = "MyCMRI_Preferences"
    private const val KEY_USERNAME = "username"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getUsername(context: Context): String? {
        return getPreferences(context).getString(KEY_USERNAME, null)
    }

    fun saveUsername(context: Context, username: String) {
        getPreferences(context).edit().putString(KEY_USERNAME, username).apply()
    }
}


