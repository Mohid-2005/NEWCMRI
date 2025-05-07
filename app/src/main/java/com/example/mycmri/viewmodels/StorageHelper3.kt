package com.example.mycmri.viewmodels

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object StorageHelper3 {
    private const val PREFS_NAME = "MyCMRIPrefs"
    private const val ALLERGIES_KEY = "allergies"

    fun saveAllergies(context: Context, allergies: List<String>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(allergies)
        prefs.edit().putString(ALLERGIES_KEY, json).apply()
    }

    fun getAllergies(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(ALLERGIES_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(json, type)
        } else emptyList()
    }
}
