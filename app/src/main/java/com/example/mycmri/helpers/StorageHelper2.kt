package com.example.mycmri.helpers

import android.content.Context
import android.content.SharedPreferences

object StorageHelper2 {
    private const val PREF_NAME = "medications_prefs"

    fun saveMedications(context: Context, meds: List<Triple<String, String, String>>) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val medsString = meds.joinToString("#") { "${it.first}|${it.second}|${it.third}" }
        editor.putString("medications", medsString)
        editor.apply()
    }

    fun getMedications(context: Context): List<Triple<String, String, String>> {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val medsString = sharedPref.getString("medications", "") ?: return emptyList()
        return medsString.split("#").mapNotNull {
            val parts = it.split("|")
            if (parts.size == 3) Triple(parts[0], parts[1], parts[2]) else null
        }
    }
}
