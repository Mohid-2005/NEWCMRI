package com.example.mycmri

import android.content.Context
import android.content.SharedPreferences

class StorageHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("my_cmri_prefs", Context.MODE_PRIVATE)

    fun saveDiagnoses(diagnoses: List<String>) {
        sharedPreferences.edit()
            .putStringSet("diagnoses", diagnoses.toSet())
            .apply()
    }

    fun loadDiagnoses(): List<String> {
        return sharedPreferences.getStringSet("diagnoses", emptySet())?.toList() ?: emptyList()
    }
}


