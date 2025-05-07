package com.example.mycmri.helpers

import android.content.Context
import android.util.Log
import com.example.mycmri.data.Medication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * This is functionally a View Model for the MedicationsPage. It interacts with the user-facing UI and
 * updates the database accordingly.
 */
object MedicationData {
    private const val PREF_NAME = "medications_prefs"

    // TODO: Convert to view model; tidy.
    /*
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
    */
    fun saveMedication(context: Context, med: Medication) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.w(PREF_NAME, "User not authenticated")
            return
        }

        val db = FirebaseFirestore.getInstance()
        val medMap = mapOf(
            "name" to med.name,
            "frequency" to med.frequency,
            "duration" to med.duration
        )

        db.collection("patient")
            .document(userId)
            .collection("medication")
            .add(medMap)
            .addOnSuccessListener {
                Log.d(PREF_NAME, "Medication added to Firestore")
            }
            .addOnFailureListener {
                Log.e(PREF_NAME, "Failed to add medication", it)
            }
    }

    fun getMedications(context: Context, onResult: (List<Medication>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return onResult(emptyList())
        val db = FirebaseFirestore.getInstance()

        db.collection("patient")
            .document(userId)
            .collection("medication")
            .get()
            .addOnSuccessListener { snapshot ->
                val meds = snapshot.documents.mapNotNull { doc ->
                    Log.d(PREF_NAME, "doc fields: ${doc.data}")
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val timesPerDay = doc.getString("frequency") ?: return@mapNotNull null
                    val duration = doc.getString("duration") ?: return@mapNotNull null
                    Medication(doc.id, name, timesPerDay, duration)
                }
                onResult(meds)
            }
            .addOnFailureListener {
                Log.e(PREF_NAME, "Failed to fetch medications", it)
                onResult(emptyList())
            }
    }

    fun deleteMedication(context: Context, id: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("patient")
            .document(userId)
            .collection("medication")
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.d(PREF_NAME, "Medication deleted: $id")
            }
            .addOnFailureListener {
                Log.e(PREF_NAME, "Failed to delete medication", it)
            }
    }
}
