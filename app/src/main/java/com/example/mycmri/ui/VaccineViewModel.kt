package com.example.mycmri.ui

import androidx.lifecycle.ViewModel
import com.example.mycmri.data.Vaccine
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VaccineViewModel : ViewModel(){
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val patientDoc = userId?.let {db.collection("patient").document(it)}

    val vaccines = listOf(
        Vaccine("covid-19", "COVID-19"),
        Vaccine("influenza", "Influenza (Flu)"),
        Vaccine("hepb", "Hepatitis B"),
        Vaccine("tetanus", "Tetanus"),
        Vaccine("mmr", "MMR (Measles, Mumps, Rubella)"),
        Vaccine("hpv", "Human Papillomavirus"),
        Vaccine("polio", "Polio Vaccine"),
        Vaccine("varicella", "Varicella (Chickenpox)"),
        Vaccine("meningococcal", "Meningococcal"),
        Vaccine("pneumococcal", "Pneumococcal Vaccine")
    )

    private val _checkedState = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val checkedState = _checkedState.asStateFlow()

    init {
        loadPatientVaccines()
    }


/**
 * Loads the list of vaccine document references from the current patient's Firestore document,
 * compares them to the known list of vaccines, and updates the UI state to reflect which vaccines
 * are currently selected (i.e., present in the patient's 'vaccine' array).
 *
 * This method fetches the 'vaccine' field from the Firestore document corresponding to the
 * currently authenticated user, which is expected to be a list of DocumentReference objects.
 * It then matches each reference's document ID against the internal list of known vaccine IDs,
 * updating the UI state map accordingly.
 */
    private fun loadPatientVaccines() {
        patientDoc?.get()?.addOnSuccessListener { snapshot ->
            val refs = snapshot.get("vaccine") as? List<DocumentReference> ?: emptyList()
            val current = vaccines.associate { vaccine ->
                val isChecked = refs.any { ref ->
                    ref.id == vaccine.docId
                }
                vaccine.docId to isChecked
            }
            _checkedState.value = current
        }
    }

/**
 * Calls the Firebase Firestore database to add or remove a given vaccine from a patient document
 * based on the isChecked status within the app.
 * @param vaccine the vaccine to be updated (removed/added) to the patient document in Firebase
 * @param isChecked the true/false status of a Vaccine being applied to a Patient document field
 */
fun toggleVaccine(vaccine: Vaccine, isChecked: Boolean) {
    val vaccineRef = db.collection("vaccines").document(vaccine.docId)
    if (patientDoc == null) return

    if (isChecked) {
        patientDoc.update("vaccine", FieldValue.arrayUnion(vaccineRef)).addOnSuccessListener {
            loadPatientVaccines()
        }
    } else {
        patientDoc.update("vaccine", FieldValue.arrayRemove(vaccineRef)).addOnSuccessListener {
            loadPatientVaccines()
        }
    }

    _checkedState.value = _checkedState.value.toMutableMap().apply {
        this[vaccine.docId] = isChecked
    }
}
}